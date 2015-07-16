package by.toggi.rxbsuir.mvp.presenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import by.toggi.rxbsuir.db.model.Lesson;
import by.toggi.rxbsuir.mvp.Presenter;
import by.toggi.rxbsuir.mvp.view.WeekView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WeekPresenter implements Presenter<WeekView> {

    private List<Lesson> mLessonList = new ArrayList<>();
    private WeekView mWeekView;
    private int mWeekNumber;

    @Inject
    public WeekPresenter(int weekNumber) {
        mWeekNumber = weekNumber;
    }

    @Override
    public void onCreate() {
        if (isViewAttached()) {
            mWeekView.showLoading();
        }
    }

    private void showFilteredLessonList() {
        Observable.from(mLessonList).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.computation())
                .filter(lesson -> lesson.getWeekNumberList().contains(mWeekNumber))
                .toList()
                .subscribe(scheduleList -> {
                    if (isViewAttached()) {
                        mWeekView.showLessonList(scheduleList);
                    }
                });
    }

    @Override
    public void onDestroy() {
        detachView();
    }

    @Override
    public void attachView(WeekView view) {
        mWeekView = view;
        showFilteredLessonList();
    }

    @Override
    public void detachView() {
        mWeekView = null;
    }

    private boolean isViewAttached() {
        return mWeekView != null;
    }

}
