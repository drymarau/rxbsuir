package by.toggi.rxbsuir.mvp.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResult;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.ArrayList;
import java.util.List;

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

    private final BsuirService mService;
    private final StorIOSQLite mStorIOSQLite;
    private String mSyncId;
    private boolean mIsGroupSchedule;

    /**
     * Instantiates a new Schedule presenter.
     *
     * @param service      the bsuirService
     * @param storIOSQLite the storIOSQlite
     */
    public SchedulePresenter(BsuirService service, StorIOSQLite storIOSQLite) {
        mService = service;
        mStorIOSQLite = storIOSQLite;
    }

    /**
     * Sets group number and updates schedule list.
     *
     * @param syncId the group number
     */
    public void setSyncId(@Nullable String syncId, Boolean isGroupSchedule) {
        if (syncId != null) {
            // Set syncId and isGroupSchedule
            mSyncId = syncId;
            mIsGroupSchedule = isGroupSchedule;
            // View.showLoading()
            if (isViewAttached()) {
                getView().showLoading();
            }
            Observable.concat(
                    // If database contains syncId, View.showContent()
                    mStorIOSQLite.get()
                            .listOfObjects(Lesson.class)
                            .withQuery(Query.builder()
                                    .table(LessonEntry.TABLE_NAME)
                                    .where(LessonEntry.Query.builder(syncId, isGroupSchedule)
                                            .build()
                                            .toString())
                                    .build())
                            .prepare()
                            .createObservable()
                            .observeOn(AndroidSchedulers.mainThread())
                            .take(1)
                            .doOnNext(lessonList1 -> Timber.d("size: %s", lessonList1.size())),
                    // If database doesn't contain syncId, make a network request and store result in database
                    getLessonListFromNetworkObservable(syncId, isGroupSchedule))
                    .first(lessonList -> !lessonList.isEmpty())
                    .subscribe(lessonList -> {
                        // View.showContent()
                        if (isViewAttached()) {
                            getView().showContent();
                        }
                    }, this::onError);
        }
    }

    /**
     * Retry network request with the same group or employee.
     */
    public void retry() {
        // View.showLoading()
        if (isViewAttached()) {
            getView().showLoading();
        }
        // Make a network request with current syncId and isGroupSchedule
        getLessonListFromNetworkObservable(mSyncId, mIsGroupSchedule).subscribe(lessonList -> {
            if (isViewAttached()) {
                getView().showContent();
            }
        }, this::onError);
        // View.showContent()

    }


    /**
     * Remove current syncId from db.
     */
    public void remove(String syncId, Boolean isGroupSchedule) {
        // Remove all records from db with supplied syncId;
        Observable.concat(
                getDeleteSyncIdObservable(syncId, isGroupSchedule),
                isGroupSchedule ? getCacheGroupObservable(false) : getCacheEmployeeObservable(false)
        ).doOnError(this::onError).subscribe();
    }

    private Observable<DeleteResult> getDeleteSyncIdObservable(String syncId, boolean isGroupSchedule) {
        return mStorIOSQLite.delete()
                .byQuery(DeleteQuery.builder()
                        .table(LessonEntry.TABLE_NAME)
                        .where(LessonEntry.Query.builder(syncId, isGroupSchedule)
                                .build()
                                .toString())
                        .build())
                .prepare()
                .createObservable()
                .doOnNext(deleteResult -> Timber.d(deleteResult.toString()));
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
        detachView();
    }

    private void onNetworkSuccess(List<Lesson> lessonList, boolean isGroupSchedule) {
        Observable.concat(
                getDeleteSyncIdObservable(mSyncId, isGroupSchedule),
                mStorIOSQLite.put()
                        .objects(lessonList)
                        .prepare()
                        .createObservable(),
                isGroupSchedule ? getCacheGroupObservable(true) : getCacheEmployeeObservable(true)
        ).subscribe();
    }

    private Observable<Employee> getCacheEmployeeObservable(boolean isCached) {
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
                .observeOn(Schedulers.io())
                .filter(employeeList -> !employeeList.isEmpty())
                .map(employeeList -> employeeList.get(0))
                .doOnNext(employee -> {
                    employee.isCached = isCached;
                    mStorIOSQLite.put().object(employee).prepare().executeAsBlocking();
                });
    }

    private Observable<StudentGroup> getCacheGroupObservable(boolean isCached) {
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
                .filter(studentGroupList -> !studentGroupList.isEmpty())
                .map(studentGroupList -> studentGroupList.get(0))
                .doOnNext(studentGroup -> {
                    studentGroup.isCached = isCached;
                    mStorIOSQLite.put().object(studentGroup).prepare().executeAsBlocking();
                });
    }

    private void onError(Throwable throwable) {
        if (isViewAttached()) {
            if (throwable.getMessage() != null && throwable.getMessage().contains("org.simpleframework.xml.core.ValueRequiredException")) {
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

    private Observable<List<Lesson>> getLessonListFromNetworkObservable(@NonNull String syncId, boolean isGroupSchedule) {
        Observable<ScheduleXmlModels> scheduleXmlModelsObservable = isGroupSchedule
                ? mService.getGroupSchedule(syncId.replace("М", "M"))
                : mService.getEmployeeSchedule(syncId);
        return scheduleXmlModelsObservable.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .flatMap(scheduleXmlModels -> Observable.from(scheduleXmlModels.scheduleModelList))
                .flatMap(scheduleModel -> Observable.from(transformScheduleToLesson(scheduleModel, isGroupSchedule)))
                .toList()
                .doOnNext(lessonList -> onNetworkSuccess(lessonList, isGroupSchedule));
    }

    public enum Error {
        NETWORK, EMPTY_SCHEDULE
    }
}
