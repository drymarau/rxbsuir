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
import by.toggi.rxbsuir.rest.model.Schedule;
import by.toggi.rxbsuir.rest.model.ScheduleModel;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static by.toggi.rxbsuir.db.RxBsuirContract.LessonEntry;

/**
 * The type Schedule presenter.
 */
public class SchedulePresenter implements Presenter<ScheduleView> {

    public static final String ERROR_NO_GROUP = "error_no_group";
    public static final String ERROR_NETWORK = "error_netwok";
    public static final String ERROR_EMPTY_SCHEDULE = "error_empty_schedule";

    private Observable<List<Lesson>> mScheduleObservable;
    private ScheduleView mScheduleView;
    private BsuirService mService;
    private StorIOSQLite mStorIOSQLite;
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

    /**
     * Retry network request with the same group or employee.
     */
    public void retry() {
        if (isViewAttached()) {
            mScheduleView.showLoading();
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
        if (isViewAttached() && !mHasSynced) {
            mScheduleView.showLoading();
        }
        mSubscription = mIsGroupSchedule ? getGroupSubscription() : getEmployeeSubscription();
    }

    private Subscription getEmployeeSubscription() {
        return mScheduleObservable.subscribe(lessonList -> {
            if (lessonList == null || lessonList.isEmpty()) {
                if (!mHasSynced) {
                    getEmployeeSchedule();
                }
            } else {
                if (isViewAttached()) {
                    mScheduleView.showContent(Utils.getCurrentWeekNumber() - 1);
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
                        mScheduleView.showContent(Utils.getCurrentWeekNumber() - 1);
                    }
                }
            });
    }

    @Override
    public void onDestroy() {
        if (!mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        detachView();
    }

    @Override
    public void attachView(ScheduleView scheduleView) {
        if (scheduleView == null) {
            throw new NullPointerException("ScheduleView should not be null");
        }
        mScheduleView = scheduleView;
    }

    @Override
    public void detachView() {
        mScheduleView = null;
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
                        .createObservable()
        ).subscribe();
    }

    private void onNetworkError(Throwable throwable) {
        mHasSynced = true;
        if (isViewAttached()) {
            if (throwable.getMessage().contains("org.simpleframework.xml.core.ValueRequiredException")) {
                mScheduleView.showError(new Throwable(ERROR_EMPTY_SCHEDULE));
            } else {
                mScheduleView.showError(new Throwable(ERROR_NETWORK));
            }
        }
    }

    private List<Lesson> transformScheduleToLesson(ScheduleModel model, boolean isGroupSchedule) {
        List<Lesson> lessonList = new ArrayList<>(model.scheduleList.size());
        for (Schedule schedule : model.scheduleList) {
            lessonList.add(new Lesson(null, schedule, model.weekDay, isGroupSchedule));
        }
        return lessonList;
    }

    private boolean isViewAttached() {
        return mScheduleView != null;
    }
}
