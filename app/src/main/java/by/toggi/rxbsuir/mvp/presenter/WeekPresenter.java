package by.toggi.rxbsuir.mvp.presenter;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import javax.inject.Inject;

import by.toggi.rxbsuir.db.model.Lesson;
import by.toggi.rxbsuir.mvp.Presenter;
import by.toggi.rxbsuir.mvp.view.WeekView;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

import static by.toggi.rxbsuir.db.RxBsuirContract.LessonEntry;

public class WeekPresenter implements Presenter<WeekView> {

    private WeekView mWeekView;
    private int mWeekNumber;
    private StorIOSQLite mStorIOSQLite;
    private Subscription mSubscription;

    @Inject
    public WeekPresenter(int weekNumber, StorIOSQLite storIOSQLite) {
        mWeekNumber = weekNumber;
        mStorIOSQLite = storIOSQLite;
    }

    @Override
    public void onCreate() {
        showLessonList();
    }

    private void showLessonList() {
        mSubscription = mStorIOSQLite.get()
                .listOfObjects(Lesson.class)
                .withQuery(Query.builder()
                        .table(LessonEntry.TABLE_NAME)
                        .where(LessonEntry.filterByWeekNumber(mWeekNumber))
                        .build())
                .prepare()
                .createObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lessonList -> {
                    if (isViewAttached()) {
                        mWeekView.showLessonList(lessonList);
                    }
                });
    }

    @Override
    public void onDestroy() {
        detachView();
        if (!mSubscription.isUnsubscribed()) {
            Timber.d(mSubscription.toString());
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void attachView(WeekView view) {
        mWeekView = view;
        showLessonList();
    }

    @Override
    public void detachView() {
        mWeekView = null;
    }

    private boolean isViewAttached() {
        return mWeekView != null;
    }

}
