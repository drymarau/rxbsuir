package by.toggi.rxbsuir.mvp.presenter;

import android.support.annotation.Nullable;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import by.toggi.rxbsuir.Utils;
import by.toggi.rxbsuir.db.model.Lesson;
import by.toggi.rxbsuir.mvp.Presenter;
import by.toggi.rxbsuir.mvp.view.ScheduleView;
import by.toggi.rxbsuir.rest.BsuirService;
import by.toggi.rxbsuir.rest.model.Employee;
import by.toggi.rxbsuir.rest.model.Schedule;
import by.toggi.rxbsuir.rest.model.ScheduleModel;
import by.toggi.rxbsuir.rest.model.ScheduleXmlModels;
import by.toggi.rxbsuir.rest.model.StudentGroup;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static by.toggi.rxbsuir.activity.ScheduleActivity.KEY_IS_GROUP_SCHEDULE;
import static by.toggi.rxbsuir.activity.ScheduleActivity.KEY_SYNC_ID;
import static by.toggi.rxbsuir.db.RxBsuirContract.EmployeeEntry;
import static by.toggi.rxbsuir.db.RxBsuirContract.LessonEntry;
import static by.toggi.rxbsuir.db.RxBsuirContract.StudentGroupEntry;

public class SchedulePresenter extends Presenter<ScheduleView> {

    private final BsuirService mService;
    private final StorIOSQLite mStorIOSQLite;
    private Observable<List<Lesson>> mLessonListObservable;
    private String mSyncId;
    private boolean mHasSynced = false;
    private boolean mIsGroupSchedule;
    private Subscription mSubscription;

    /**
     * Instantiates a new Schedule presenter.
     *
     * @param service      the bsuirService
     * @param storIOSQLite the stor iOSQ lite
     */
    @Inject
    public SchedulePresenter(@Named(KEY_IS_GROUP_SCHEDULE) boolean isGroupSchedule, @Nullable @Named(KEY_SYNC_ID) String syncId, BsuirService service, StorIOSQLite storIOSQLite) {
        mService = service;
        mStorIOSQLite = storIOSQLite;
        mSyncId = syncId;
        mIsGroupSchedule = isGroupSchedule;
        mLessonListObservable = getLessonListObservable(syncId, isGroupSchedule);
    }

    /**
     * Sets group number and updates schedule list.
     *
     * @param syncId the group number
     */
    public void setSyncId(String syncId, boolean isGroupSchedule) {
        mHasSynced = false;
        mIsGroupSchedule = isGroupSchedule;
        mSyncId = syncId;
        mLessonListObservable = getLessonListObservable(syncId, isGroupSchedule);
        onCreate();
    }

    /**
     * Retry network request with the same group or employee.
     */
    public void retry() {
        if (isViewAttached()) {
            getView().showLoading();
        }
        mHasSynced = false;
        getLessonListFromNetwork(mSyncId, mIsGroupSchedule);
    }

    @Override
    public void onCreate() {
        if (isViewAttached() && !mHasSynced && mSyncId != null) {
            getView().showLoading();
        }
        mSubscription = mLessonListObservable.subscribe(lessonList -> {
            if (lessonList == null || lessonList.isEmpty()) {
                if (!mHasSynced) {
                    getLessonListFromNetwork(mSyncId, mIsGroupSchedule);
                }
            } else {
                if (isViewAttached()) {
                    getView().showContent(Utils.getCurrentWeekNumber() - 1);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        Utils.unsubscribe(mSubscription);
        detachView();
    }

    private void onNetworkSuccess(List<Lesson> lessonList, boolean isGroupSchedule) {
        mHasSynced = true;
        String whereQuery = LessonEntry.filterByGroup(mSyncId);
        if (!isGroupSchedule) {
            whereQuery = LessonEntry.filterByEmployee(mSyncId);
        }
        Observable.concat(
                mStorIOSQLite.delete()
                        .byQuery(DeleteQuery.builder()
                                .table(LessonEntry.TABLE_NAME)
                                .where(whereQuery)
                                .build())
                        .prepare()
                        .createObservable(),
                mStorIOSQLite.put()
                        .objects(lessonList)
                        .prepare()
                        .createObservable(),
                isGroupSchedule ? getCacheGroupObservable() : getCacheEmployeeObservable()
        ).subscribe();
    }

    private Observable<Employee> getCacheEmployeeObservable() {
        return mStorIOSQLite.get()
                .listOfObjects(Employee.class)
                .withQuery(Query.builder()
                        .table(EmployeeEntry.TABLE_NAME)
                        .where(EmployeeEntry.COL_ID + " = ?")
                        .whereArgs(mSyncId)
                        .build())
                .prepare()
                .createObservable()
                .take(1)
                .map(employeeList -> employeeList.get(0))
                .doOnNext(employee -> {
                    employee.isCached = true;
                    mStorIOSQLite.put().object(employee).prepare().createObservable().subscribe();
                });
    }

    private Observable<StudentGroup> getCacheGroupObservable() {
        return mStorIOSQLite.get()
                .listOfObjects(StudentGroup.class)
                .withQuery(Query.builder()
                        .table(StudentGroupEntry.TABLE_NAME)
                        .where(StudentGroupEntry.COL_NAME + " = ?")
                        .whereArgs(mSyncId)
                        .build())
                .prepare()
                .createObservable()
                .take(1)
                .observeOn(Schedulers.io())
                .map(studentGroupList -> studentGroupList.get(0))
                .doOnNext(studentGroup -> {
                    studentGroup.isCached = true;
                    mStorIOSQLite.put().object(studentGroup).prepare().createObservable().subscribe();
                });
    }

    private void onNetworkError(Throwable throwable) {
        mHasSynced = true;
        if (isViewAttached()) {
            if (throwable.getMessage().contains("org.simpleframework.xml.core.ValueRequiredException")) {
                getView().showError(Error.EMPTY_SCHEDULE);
            } else {
                getView().showError(Error.NETWORK);
            }
        }
    }

    private List<Lesson> transformScheduleToLesson(ScheduleModel model, boolean isGroupSchedule) {
        List<Lesson> lessonList = new ArrayList<>(model.scheduleList.size());
        for (Schedule schedule : model.scheduleList) {
            lessonList.add(new Lesson(null, mSyncId, schedule, Utils.convertWeekdayToDayOfWeek(model.weekDay), isGroupSchedule));
        }
        return lessonList;
    }

    private Observable<List<Lesson>> getLessonListObservable(String syncId, boolean isGroupSchedule) {
        String whereQuery = isGroupSchedule
                ? LessonEntry.filterByGroup(syncId)
                : LessonEntry.filterByEmployee(syncId);
        return mStorIOSQLite.get()
                .listOfObjects(Lesson.class)
                .withQuery(Query.builder()
                        .table(LessonEntry.TABLE_NAME)
                        .where(whereQuery)
                        .build())
                .prepare()
                .createObservable()
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void getLessonListFromNetwork(String syncId, boolean isGroupSchedule) {
        if (syncId != null) {
            Observable<ScheduleXmlModels> scheduleXmlModelsObservable = isGroupSchedule
                    ? mService.getGroupSchedule(syncId.replace("М", "M"))
                    : mService.getEmployeeSchedule(syncId);
            scheduleXmlModelsObservable.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .flatMap(scheduleXmlModels -> Observable.from(scheduleXmlModels.scheduleModelList))
                    .flatMap(scheduleModel -> Observable.from(transformScheduleToLesson(scheduleModel, isGroupSchedule)))
                    .toList()
                    .subscribe(lessonList -> onNetworkSuccess(lessonList, isGroupSchedule), this::onNetworkError);
        }
    }

    public enum Error {
        NETWORK, EMPTY_SCHEDULE
    }
}
