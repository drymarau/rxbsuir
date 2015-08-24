package by.toggi.rxbsuir.mvp.presenter;

import android.support.annotation.Nullable;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import by.toggi.rxbsuir.Utils;
import by.toggi.rxbsuir.db.model.Lesson;
import by.toggi.rxbsuir.mvp.Presenter;
import by.toggi.rxbsuir.mvp.view.WeekView;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static by.toggi.rxbsuir.PreferenceHelper.IS_GROUP_SCHEDULE;
import static by.toggi.rxbsuir.PreferenceHelper.SYNC_ID;
import static by.toggi.rxbsuir.db.RxBsuirContract.LessonEntry;

public class WeekPresenter extends Presenter<WeekView> {

    private final StorIOSQLite mStorIOSQLite;
    private final int mWeekNumber;
    private Observable<List<Lesson>> mScheduleObservable;
    private int mSubgroupNumber = 0;
    private String mSyncId;
    private Subscription mSubscription;

    @Inject
    public WeekPresenter(@Named(IS_GROUP_SCHEDULE) boolean isGroupSchedule, @Nullable @Named(SYNC_ID) String syncId, int weekNumber, StorIOSQLite storIOSQLite) {
        mWeekNumber = weekNumber;
        mStorIOSQLite = storIOSQLite;
        mSyncId = syncId;
        if (isGroupSchedule) {
            mScheduleObservable = getGroupScheduleObservable(mSyncId, mSubgroupNumber, mWeekNumber);
        } else {
            mScheduleObservable = getEmployeeListObservable(mSyncId, mSubgroupNumber, mWeekNumber);
        }
    }

    /**
     * Sets group number and updates the list.
     *
     * @param syncId the group number
     */
    public void setSyncId(String syncId, boolean isGroupSchedule) {
        mSyncId = syncId;
        if (isGroupSchedule) {
            mScheduleObservable = getGroupScheduleObservable(mSyncId, mSubgroupNumber, mWeekNumber);
        } else {
            mScheduleObservable = getEmployeeListObservable(mSyncId, mSubgroupNumber, mWeekNumber);
        }
        onCreate();
    }

    /**
     * Sets subgroup number.
     *
     * @param subgroup1 the subgroup 1 state
     * @param subgroup2 the subgroup 2 state
     */
    public void setSubgroupNumber(boolean subgroup1, boolean subgroup2, boolean isGroupSchedule) {
        if (subgroup1 && subgroup2) {
            mSubgroupNumber = 0;
        } else if (!subgroup1 && !subgroup2) {
            mSubgroupNumber = 3;
        } else {
            if (subgroup1) {
                mSubgroupNumber = 1;
            } else {
                mSubgroupNumber = 2;
            }
        }
        if (isGroupSchedule) {
            mScheduleObservable = getGroupScheduleObservable(mSyncId, mSubgroupNumber, mWeekNumber);
        } else {
            mScheduleObservable = getEmployeeListObservable(mSyncId, mSubgroupNumber, mWeekNumber);
        }
        onCreate();
    }

    @Override
    public void onCreate() {
        Utils.unsubscribe(mSubscription);
        mSubscription = mScheduleObservable.subscribe(lessons -> {
            if (lessons.size() > 0) {
                showLessonList(lessons);
            }
        });
    }

    @Override
    public void onDestroy() {
        Utils.unsubscribe(mSubscription);
        detachView();
    }

    @Override
    public String getTag() {
        return this.getClass().getSimpleName() + "_" + mWeekNumber;
    }

    private Observable<List<Lesson>> getGroupScheduleObservable(@Nullable String syncId, int subgroupNumber, int weekNumber) {
        return syncId == null ? Observable.empty() : mStorIOSQLite.get()
                .listOfObjects(Lesson.class)
                .withQuery(Query.builder()
                        .table(LessonEntry.TABLE_NAME)
                        .where(LessonEntry.filterByGroupSubgroupAndWeek(syncId, subgroupNumber, weekNumber))
                        .build())
                .prepare()
                .createObservable()
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<List<Lesson>> getEmployeeListObservable(@Nullable String syncId, int subgroupNumber, int weekNumber) {
        return syncId == null ? Observable.empty() : mStorIOSQLite.get()
                .listOfObjects(Lesson.class)
                .withQuery(Query.builder()
                        .table(LessonEntry.TABLE_NAME)
                        .where(LessonEntry.filterByEmployeeSubgroupAndWeek(syncId, subgroupNumber, weekNumber))
                        .build())
                .prepare()
                .createObservable()
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void showLessonList(List<Lesson> lessonList) {
        if (isViewAttached()) {
            getView().showLessonList(lessonList);
        }
    }
}
