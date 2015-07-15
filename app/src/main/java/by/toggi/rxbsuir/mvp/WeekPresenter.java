package by.toggi.rxbsuir.mvp;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import by.toggi.rxbsuir.db.model.Lesson;
import by.toggi.rxbsuir.rest.BsuirService;
import by.toggi.rxbsuir.rest.model.Schedule;
import by.toggi.rxbsuir.rest.model.ScheduleModel;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WeekPresenter implements Presenter<WeekView> {

    private List<Lesson> mLessonList = new ArrayList<>();
    private WeekView mWeekView;
    private BsuirService mService;
    private int mWeekNumber;

    @Inject
    public WeekPresenter(BsuirService service, int weekNumber) {
        mService = service;
        mWeekNumber = weekNumber;
    }

    @Override
    public void onCreate() {
        if (isViewAttached()) {
            mWeekView.showLoading();
        }
        mService.getGroupSchedule(111801).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .flatMap(scheduleXmlModels -> Observable.from(scheduleXmlModels.scheduleModelList))
                .flatMap(scheduleModel -> Observable.from(transformScheduleToLesson(scheduleModel)))
                .toList()
                .subscribe(this::onSuccess, this::onError);
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

    private void onSuccess(List<Lesson> lessonList) {
        mLessonList = lessonList;
        showFilteredLessonList();
    }

    private void onError(Throwable throwable) {
        if (isViewAttached()) {
            mWeekView.showError(throwable);
        }
    }

    private boolean isViewAttached() {
        return mWeekView != null;
    }

    private List<Lesson> transformScheduleToLesson(ScheduleModel model) {
        List<Lesson> lessonList = new ArrayList<>(model.scheduleList.size());
        for (Schedule schedule : model.scheduleList) {
            lessonList.add(new Lesson(schedule, model.weekDay));
        }
        return lessonList;
    }
}
