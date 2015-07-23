package by.toggi.rxbsuir.mvp.presenter;

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

    @Inject
    public WeekPresenter(String groupNumber, int weekNumber, StorIOSQLite storIOSQLite) {
        mWeekNumber = weekNumber;
        mStorIOSQLite = storIOSQLite;
        mListObservable = mStorIOSQLite.get()
                .listOfObjects(Lesson.class)
                .withQuery(Query.builder()
                        .table(LessonEntry.TABLE_NAME)
                        .where(LessonEntry.filterByGroupNumberAndWeekNumber(groupNumber, weekNumber))
                        .build())
                .prepare()
                .createObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .cache();
    }

    public void setGroupNumber(String groupNumber) {
        mListObservable = getListObservable(groupNumber, mWeekNumber);
        onCreate();
    }

    private Observable<List<Lesson>> getListObservable(String groupNumber, int weekNumber) {
        return mStorIOSQLite.get()
                .listOfObjects(Lesson.class)
                .withQuery(Query.builder()
                        .table(LessonEntry.TABLE_NAME)
                        .where(LessonEntry.filterByGroupNumberAndWeekNumber(groupNumber, weekNumber))
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
    public void attachView(WeekView view) {
        mWeekView = view;
    }

    @Override
    public void detachView() {
        mWeekView = null;
    }

    private boolean isViewAttached() {
        return mWeekView != null;
    }

}
