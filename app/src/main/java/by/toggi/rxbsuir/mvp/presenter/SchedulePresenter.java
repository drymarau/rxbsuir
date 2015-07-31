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
import by.toggi.rxbsuir.activity.ScheduleActivity;
import by.toggi.rxbsuir.db.model.Lesson;
import by.toggi.rxbsuir.mvp.Presenter;
import by.toggi.rxbsuir.mvp.view.ScheduleView;
import by.toggi.rxbsuir.rest.BsuirService;
import by.toggi.rxbsuir.rest.model.Employee;
import by.toggi.rxbsuir.rest.model.Schedule;
import by.toggi.rxbsuir.rest.model.ScheduleModel;
import by.toggi.rxbsuir.rest.model.StudentGroup;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static by.toggi.rxbsuir.db.RxBsuirContract.EmployeeEntry;
import static by.toggi.rxbsuir.db.RxBsuirContract.LessonEntry;
import static by.toggi.rxbsuir.db.RxBsuirContract.StudentGroupEntry;

/**
 * The type Schedule presenter.
 */
public class SchedulePresenter extends Presenter<ScheduleView> {

    public enum Error {
        NETWORK, EMPTY_SCHEDULE
    }

    private final BsuirService mService;
    private final StorIOSQLite mStorIOSQLite;
    private Observable<List<Lesson>> mScheduleObservable;
    private String mGroupNumber;
    private String mEmployeeId;
    private boolean mHasSynced = false;
    private boolean mIsGroupSchedule;
    private Subscription mSubscription;

    /**
     * Instantiates a new Schedule presenter.
     *
     * @param service      the service
     * @param storIOSQLite the stor iOSQ lite
     */
    @Inject
    public SchedulePresenter(boolean isGroupSchedule, @Nullable @Named(ScheduleActivity.KEY_GROUP_NUMBER) String groupNumber, @Nullable @Named(ScheduleActivity.KEY_EMPLOYEE_ID) String employeeId, BsuirService service, StorIOSQLite storIOSQLite) {
        mService = service;
        mStorIOSQLite = storIOSQLite;
        mGroupNumber = groupNumber;
        mEmployeeId = employeeId;
        mIsGroupSchedule = isGroupSchedule;
        if (mIsGroupSchedule) {
            mScheduleObservable = getGroupLessonListObservable(groupNumber);
        } else {
            mScheduleObservable = getEmployeeLessonListObservable(employeeId);
        }
    }

    /**
     * Sets group number and updates schedule list.
     *
     * @param groupNumber the group number
     */
    public void setGroupNumber(String groupNumber) {
        mHasSynced = false;
        mIsGroupSchedule = true;
        mGroupNumber = groupNumber;
        mScheduleObservable = getGroupLessonListObservable(groupNumber);
        onCreate();
    }

    /**
     * Sets employeeId and updates schedule list.
     *
     * @param employeeId the group number
     */
    public void setEmployeeId(String employeeId) {
        mHasSynced = false;
        mIsGroupSchedule = false;
        mEmployeeId = employeeId;
        mScheduleObservable = getEmployeeLessonListObservable(employeeId);
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
        if (mIsGroupSchedule) {
            getStudentGroupSchedule();
        } else {
            getEmployeeSchedule();
        }
    }

    @Override
    public void onCreate() {
        if (isViewAttached() && !mHasSynced && (mEmployeeId != null || mGroupNumber != null)) {
            getView().showLoading();
        }
        mSubscription = mIsGroupSchedule ? getGroupSubscription() : getEmployeeSubscription();
    }

    @Override
    public void onDestroy() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        detachView();
    }

    private void onNetworkSuccess(List<Lesson> lessonList, boolean isGroupSchedule) {
        mHasSynced = true;
        String whereQuery = LessonEntry.filterByGroup(mGroupNumber);
        if (!isGroupSchedule) {
            whereQuery = LessonEntry.filterByEmployee(mEmployeeId);
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
                        .whereArgs(mEmployeeId)
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
                        .whereArgs(mGroupNumber)
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
        String syncId = isGroupSchedule ? mGroupNumber : mEmployeeId;
        for (Schedule schedule : model.scheduleList) {
            lessonList.add(new Lesson(null, syncId, schedule, model.weekDay, isGroupSchedule));
        }
        return lessonList;
    }

    private Observable<List<Lesson>> getEmployeeLessonListObservable(String employeeId) {
        return mStorIOSQLite.get()
                .listOfObjects(Lesson.class)
                .withQuery(Query.builder()
                        .table(LessonEntry.TABLE_NAME)
                        .where(LessonEntry.filterByEmployee(employeeId))
                        .build())
                .prepare()
                .createObservable()
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<List<Lesson>> getGroupLessonListObservable(String groupNumber) {
        return mStorIOSQLite.get()
                .listOfObjects(Lesson.class)
                .withQuery(Query.builder()
                        .table(LessonEntry.TABLE_NAME)
                        .where(LessonEntry.filterByGroup(groupNumber))
                        .build())
                .prepare()
                .createObservable()
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void getStudentGroupSchedule() {
        if (mGroupNumber != null) {
            mService.getGroupSchedule(mGroupNumber).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .flatMap(scheduleXmlModels -> Observable.from(scheduleXmlModels.scheduleModelList))
                    .flatMap(scheduleModel -> Observable.from(transformScheduleToLesson(scheduleModel, true)))
                    .toList()
                    .subscribe(lessonList -> onNetworkSuccess(lessonList, true), this::onNetworkError);
        }
    }

    private void getEmployeeSchedule() {
        if (mEmployeeId != null) {
            mService.getEmployeeSchedule(mEmployeeId).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                    .flatMap(scheduleXmlModels -> Observable.from(scheduleXmlModels.scheduleModelList))
                    .flatMap(scheduleModel -> Observable.from(transformScheduleToLesson(scheduleModel, false)))
                    .toList()
                    .subscribe(lessonList -> onNetworkSuccess(lessonList, false), this::onNetworkError);
        }
    }

    private Subscription getEmployeeSubscription() {
        return mScheduleObservable.subscribe(lessonList -> {
            if (lessonList == null || lessonList.isEmpty()) {
                if (!mHasSynced) {
                    getEmployeeSchedule();
                }
            } else {
                if (isViewAttached()) {
                    getView().showContent(Utils.getCurrentWeekNumber() - 1);
                }
            }
        });
    }

    private Subscription getGroupSubscription() {
        return mScheduleObservable.subscribe(lessonList -> {
            if (lessonList == null || lessonList.isEmpty()) {
                if (!mHasSynced) {
                    getStudentGroupSchedule();
                }
            } else {
                if (isViewAttached()) {
                    getView().showContent(Utils.getCurrentWeekNumber() - 1);
                }
            }
        });
    }
}
