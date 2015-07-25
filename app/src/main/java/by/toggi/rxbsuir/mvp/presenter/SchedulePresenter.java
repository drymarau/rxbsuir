package by.toggi.rxbsuir.mvp.presenter;

import android.support.annotation.Nullable;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import by.toggi.rxbsuir.Utils;
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

    private Observable<List<Lesson>> mLessonListObservable;
    private ScheduleView mScheduleView;
    private BsuirService mService;
    private StorIOSQLite mStorIOSQLite;
    private String mGroupNumber;
    private Subscription mSubscription;
    private boolean mHasSynced = false;

    /**
     * Instantiates a new Schedule presenter.
     *
     * @param service      the service
     * @param storIOSQLite the stor iOSQ lite
     */
    @Inject
    public SchedulePresenter(@Nullable String groupNumber, BsuirService service, StorIOSQLite storIOSQLite) {
        mService = service;
        mStorIOSQLite = storIOSQLite;
        mGroupNumber = groupNumber;
        mLessonListObservable = getLessonListObservable(groupNumber);
    }

    private Observable<List<Lesson>> getLessonListObservable(@Nullable String groupNumber) {
        return mStorIOSQLite.get()
                .listOfObjects(Lesson.class)
                .withQuery(Query.builder()
                        .table(LessonEntry.TABLE_NAME)
                        .where(LessonEntry.filterByGroupNumber(groupNumber))
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
        mGroupNumber = groupNumber;
        mLessonListObservable = getLessonListObservable(groupNumber);
        onCreate();
    }

    /**
     * Gets student group schedule.
     */
    public void getStudentGroupSchedule() {
        mService.getGroupSchedule(mGroupNumber).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .flatMap(scheduleXmlModels -> Observable.from(scheduleXmlModels.scheduleModelList))
                .flatMap(scheduleModel -> Observable.from(transformScheduleToLesson(scheduleModel)))
                .toList()
                .subscribe(this::onNetworkSuccess, this::onNetworkError);
    }

    /**
     * Retry network request with the same group.
     */
    public void retry() {
        if (isViewAttached()) {
            mScheduleView.showLoading();
        }
        mHasSynced = false;
        getStudentGroupSchedule();
    }

    @Override
    public void onCreate() {
        if (isViewAttached() && !mHasSynced) {
            mScheduleView.showLoading();
        }
        mSubscription = mLessonListObservable.subscribe(lessonList -> {
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

    private void onNetworkSuccess(List<Lesson> lessonList) {
        mHasSynced = true;
        Observable.concat(
                mStorIOSQLite.delete()
                        .byQuery(DeleteQuery.builder()
                                .table(LessonEntry.TABLE_NAME)
                                .where(LessonEntry.filterByGroupNumber(mGroupNumber))
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
            mScheduleView.showError(throwable);
        }
    }

    private List<Lesson> transformScheduleToLesson(ScheduleModel model) {
        List<Lesson> lessonList = new ArrayList<>(model.scheduleList.size());
        for (Schedule schedule : model.scheduleList) {
            lessonList.add(new Lesson(null, schedule, model.weekDay, true));
        }
        return lessonList;
    }

    private boolean isViewAttached() {
        return mScheduleView != null;
    }
}
