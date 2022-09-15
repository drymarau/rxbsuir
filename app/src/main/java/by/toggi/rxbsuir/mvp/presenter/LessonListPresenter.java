package by.toggi.rxbsuir.mvp.presenter;

import static by.toggi.rxbsuir.db.RxBsuirContract.LessonEntry;

import androidx.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;

import javax.inject.Inject;

import by.toggi.rxbsuir.Utils;
import by.toggi.rxbsuir.db.model.Lesson;
import by.toggi.rxbsuir.mvp.Presenter;
import by.toggi.rxbsuir.mvp.view.LessonListView;
import rx.Observable;
import rx.Subscription;

public class LessonListPresenter extends Presenter<LessonListView> {

    private final Type mType;
    private SubgroupFilter mSubgroupFilter;
    private String mSyncId;
    private boolean mIsGroupSchedule;
    private String mSearchQuery;
    private Subscription mSubscription;

    @Inject
    public LessonListPresenter(Type type, SubgroupFilter subgroupFilter) {
        mType = type;
        mSubgroupFilter = subgroupFilter;
    }

    @Override
    public void onCreate() {
        Utils.unsubscribe(mSubscription);
        mSubscription = getLessonListObservable(getLessonQuery())
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

    /**
     * Sets group number and updates the list.
     *
     * @param syncId the group number
     */
    public void setSyncId(@Nullable String syncId, Boolean isGroupSchedule) {
        mSyncId = syncId;
        mIsGroupSchedule = isGroupSchedule;
        onCreate();
    }

    /**
     * Sets subgroup filter.
     *
     * @param filter subgroup filter
     */
    public void setSubgroupFilter(SubgroupFilter filter) {
        mSubgroupFilter = filter;
        onCreate();
    }

    /**
     * Sets searchQuery.
     *
     * @param searchQuery searchQuery query
     */
    public void setSearchQuery(String searchQuery) {
        mSearchQuery = searchQuery;
        onCreate();
    }

    private LessonEntry.Query getLessonQuery() {
        LessonEntry.Query.Builder builder = LessonEntry.Query.builder(mSyncId, mIsGroupSchedule)
                .subgroupFilter(mSubgroupFilter)
                .search(mSearchQuery);
        switch (mType) {
            case TODAY:
                LocalDate today = LocalDate.now();
                return builder.weekNumber(Utils.getWeekNumber(today))
                        .weekDay(today.getDayOfWeek())
                        .build();
            case TOMORROW:
                LocalDate tomorrow = LocalDate.now().plusDays(1);
                return builder.weekNumber(Utils.getWeekNumber(tomorrow))
                        .weekDay(tomorrow.getDayOfWeek())
                        .build();
            case WEEK_ONE:
            case WEEK_TWO:
            case WEEK_THREE:
            case WEEK_FOUR:
                return builder.weekNumber(mType.weekNumber()).build();
            default:
                return null;
        }
    }

    private Observable<List<Lesson>> getLessonListObservable(LessonEntry.Query query) {
        return Observable.never();
    }

    private void showLessonList(List<Lesson> lessonList) {
        if (isViewAttached()) {
            getView().showLessonList(lessonList);
        }
    }

    @Override
    public String getTag() {
        return LessonListPresenter.class.getSimpleName() + "_" + mType;
    }

    public enum SubgroupFilter {

        BOTH, FIRST, SECOND, NONE

    }

    public enum Type {

        TODAY(0), TOMORROW(0), WEEK_ONE(1), WEEK_TWO(2), WEEK_THREE(3), WEEK_FOUR(4);

        private final int mWeekNumber;

        Type(int weekNumber) {
            mWeekNumber = weekNumber;
        }

        public int weekNumber() {
            return mWeekNumber;
        }
    }
}
