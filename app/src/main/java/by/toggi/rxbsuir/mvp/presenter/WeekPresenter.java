package by.toggi.rxbsuir.mvp.presenter;

import android.support.annotation.Nullable;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.List;

import javax.inject.Inject;

import by.toggi.rxbsuir.db.model.Lesson;
import by.toggi.rxbsuir.mvp.Presenter;
import by.toggi.rxbsuir.mvp.view.WeekView;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static by.toggi.rxbsuir.db.RxBsuirContract.LessonEntry;

public class WeekPresenter implements Presenter<WeekView> {

    private WeekView mWeekView;
    private Observable<List<Lesson>> mListObservable;
    private Subscription mSubscription;
    private StorIOSQLite mStorIOSQLite;
    private int mWeekNumber;
    private int mSubgroupNumber = 0;
    private String mGroupNumber;

    @Inject
    public WeekPresenter(@Nullable String groupNumber, int weekNumber, StorIOSQLite storIOSQLite) {
        mWeekNumber = weekNumber;
        mStorIOSQLite = storIOSQLite;
        mGroupNumber = groupNumber;
        if (mGroupNumber != null) {
            mListObservable = getListObservable(mGroupNumber, mSubgroupNumber, mWeekNumber);
        } else {
            mListObservable = Observable.empty();
        }
    }


    /**
     * Sets group number and updates the list.
     *
     * @param groupNumber the group number
     */
    public void setGroupNumber(String groupNumber) {
        mGroupNumber = groupNumber;
        mListObservable = getListObservable(mGroupNumber, mSubgroupNumber, mWeekNumber);
        onCreate();
    }

    public void setSubgroupNumber(boolean subgroup1, boolean subgroup2) {
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
        mListObservable = getListObservable(mGroupNumber, mSubgroupNumber, mWeekNumber);
        onCreate();
    }

    private Observable<List<Lesson>> getListObservable(String groupNumber, int subgroupNumber, int weekNumber) {
        return mStorIOSQLite.get()
                .listOfObjects(Lesson.class)
                .withQuery(Query.builder()
                        .table(LessonEntry.TABLE_NAME)
                        .where(LessonEntry.filterByGroupSubgroupAndWeek(groupNumber, subgroupNumber, weekNumber))
                        .build())
                .prepare()
                .createObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .cache();
    }

    @Override
    public void onCreate() {
        mSubscription = mListObservable.subscribe(this::showLessonList);
    }

    private void showLessonList(List<Lesson> lessonList) {
        if (isViewAttached()) {
            mWeekView.showLessonList(lessonList);
        }
    }

    @Override
    public void onDestroy() {
        if (!mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        detachView();
    }

    @Override
    public void attachView(WeekView weekView) {
        if (weekView == null) {
            throw new NullPointerException("WeekView should not be null");
        }
        mWeekView = weekView;
    }

    @Override
    public void detachView() {
        mWeekView = null;
    }

    private boolean isViewAttached() {
        return mWeekView != null;
    }

}
