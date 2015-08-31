package by.toggi.rxbsuir.mvp.presenter;

import android.support.annotation.Nullable;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

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

public class WeekPresenter extends Presenter<LessonListView> {

    private final StorIOSQLite mStorIOSQLite;
    private final int mWeekNumber;
    private SubgroupFilter mSubgroupFilter = SubgroupFilter.BOTH;
    private String mSyncId;
    private boolean mIsGroupSchedule;
    private Subscription mSubscription;
    private String mSubjectFilter = null;

    @Inject
    public WeekPresenter(StorIOSQLite storIOSQLite, int weekNumber) {
        mStorIOSQLite = storIOSQLite;
        mWeekNumber = weekNumber;
    }

    /**
     * Sets group number and updates the list.
     *
     * @param syncId the group number
     */
    public void setSyncId(@Nullable String syncId, Boolean isGroupSchedule) {
        mSyncId = syncId;
        mIsGroupSchedule = isGroupSchedule;
        mSubjectFilter = null;
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
     * Sets subject filter.
     *
     * @param subjectFilter the subject filter
     */
    public void setSubjectFilter(String subjectFilter) {
        mSubjectFilter = subjectFilter;
        onCreate();
    }

    @Override
    public void onCreate() {
        Utils.unsubscribe(mSubscription);
        mSubscription = getLessonListObservable(mSubgroupFilter, mSubjectFilter)
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

    @Override
    public String getTag() {
        return this.getClass().getSimpleName() + "_" + mWeekNumber;
    }

    private Observable<List<Lesson>> getLessonListObservable(LessonEntry.Query query) {
        return query.getSyncId() == null ? Observable.empty() : mStorIOSQLite.get()
                .listOfObjects(Lesson.class)
                .withQuery(Query.builder()
                        .table(LessonEntry.TABLE_NAME)
                        .where(query.toString())
                        .build())
                .prepare()
                .createObservable()
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<List<Lesson>> getLessonListObservable(SubgroupFilter filter, String subjectFilter) {
        return getLessonListObservable(LessonEntry.Query.builder(mSyncId, mIsGroupSchedule)
                .subgroupFilter(filter)
                .search(subjectFilter)
                .weekNumber(mWeekNumber)
                .build());
    }

    private void showLessonList(List<Lesson> lessonList) {
        if (isViewAttached()) {
            getView().showLessonList(lessonList);
        }
    }
}
