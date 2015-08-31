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
    private SubgroupFilter mSubgroupFilter = SubgroupFilter.BOTH;
    private String mSyncId;
    private boolean mIsGroupSchedule;
    private String mSearch;
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
        mIsGroupSchedule = isGroupSchedule;
        mSearch = null;
        onCreate();
    }

    /**
     * Sets subgroup number.
     *
     * @param filter subgroup filter
     */
    public void setSubgroupNumber(SubgroupFilter filter) {
        mSubgroupFilter = filter;
        onCreate();
    }

    /**
     * Sets search.
     *
     * @param search search query
     */
    public void setSearch(String search) {
        mSearch = search;
        onCreate();
    }

    @Override
    public void onCreate() {
        Utils.unsubscribe(mSubscription);
        mSubscription = getLessonListObservable(mSubgroupFilter, mSearch)
                .subscribe(lessons -> {
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

    private Observable<List<Lesson>> getLessonListObservable(SubgroupFilter filter, String search) {
        return mSyncId == null ? Observable.empty() : mStorIOSQLite.get()
                .listOfObjects(Lesson.class)
                .withQuery(Query.builder()
                        .table(LessonEntry.TABLE_NAME)
                        .where(LessonEntry.Query.builder(mSyncId, mIsGroupSchedule)
                                .weekDay(LocalDate.now().getDayOfWeek())
                                .subgroupFilter(filter)
                                .search(search)
                                .weekNumber(Utils.getCurrentWeekNumber())
                                .build()
                                .toString())
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
