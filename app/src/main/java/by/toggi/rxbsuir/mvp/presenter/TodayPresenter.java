package by.toggi.rxbsuir.mvp.presenter;

import android.support.annotation.Nullable;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import org.threeten.bp.LocalDate;

import java.util.List;

import javax.inject.Inject;

import by.toggi.rxbsuir.SubgroupFilter;
import by.toggi.rxbsuir.Utils;
import by.toggi.rxbsuir.db.model.Lesson;
import by.toggi.rxbsuir.mvp.Presenter;
import by.toggi.rxbsuir.mvp.view.LessonListView;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static by.toggi.rxbsuir.db.RxBsuirContract.LessonEntry;

public class TodayPresenter extends Presenter<LessonListView> {

    private final StorIOSQLite mStorIOSQLite;
    private Observable<List<Lesson>> mScheduleObservable;
    private SubgroupFilter mSubgroupFilter = SubgroupFilter.BOTH;
    private String mSyncId;
    private Subscription mSubscription;

    @Inject
    public TodayPresenter(StorIOSQLite storIOSQLite) {
        mStorIOSQLite = storIOSQLite;
    }

    /**
     * Sets group number and updates the list.
     *
     * @param syncId the group number
     */
    public void setSyncId(@Nullable String syncId, Boolean isGroupSchedule) {
        mSyncId = syncId;
        mScheduleObservable = getLessonListObservable(mSyncId, isGroupSchedule, mSubgroupFilter);
        onCreate();
    }

    /**
     * Sets subgroup number.
     *
     * @param filter          subgroup filter
     * @param isGroupSchedule is group schedule
     */
    public void setSubgroupNumber(SubgroupFilter filter, Boolean isGroupSchedule) {
        mScheduleObservable = getLessonListObservable(mSyncId, isGroupSchedule, filter);
        onCreate();
    }

    @Override
    public void onCreate() {
        Utils.unsubscribe(mSubscription);
        mSubscription = mScheduleObservable.subscribe(lessons -> {
            if (lessons.size() > 0) {
                showLessonList(lessons);
            } else {
                if (isViewAttached()) getView().showEmptyState();
            }
        });
    }

    @Override
    public void onDestroy() {
        Utils.unsubscribe(mSubscription);
        detachView();
    }

    private Observable<List<Lesson>> getLessonListObservable(@Nullable String syncId, boolean isGroupSchedule, SubgroupFilter filter) {
        return syncId == null ? Observable.empty() : mStorIOSQLite.get()
                .listOfObjects(Lesson.class)
                .withQuery(Query.builder()
                        .table(LessonEntry.TABLE_NAME)
                        .where(LessonEntry.getSyncIdTypeDayOfWeekWeekNumberAndSubgroupQuery(filter))
                        .whereArgs(
                                syncId,
                                isGroupSchedule ? 1 : 0,
                                LocalDate.now().getDayOfWeek().toString(),
                                "%" + Utils.getCurrentWeekNumber() + "%")
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
