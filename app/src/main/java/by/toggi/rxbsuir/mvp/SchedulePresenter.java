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

public class SchedulePresenter implements Presenter<ScheduleView> {

    private List<Lesson> mLessonList = new ArrayList<>();
    private ScheduleView mScheduleView;
    private BsuirService mService;
    private int mWeekNumber;

    @Inject
    public SchedulePresenter(BsuirService service) {
        mService = service;
    }

    public void setWeekNumber(int weekNumber) {
        mWeekNumber = weekNumber;
        showFilteredLessonList();
    }

    @Override
    public void onCreate() {
        if (isViewAttached()) {
            mScheduleView.showLoading();
        }
        mService.getGroupSchedule(211801).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
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
                        mScheduleView.showLessonList(scheduleList);
                    }
                });
    }

    @Override
    public void onDestroy() {
        detachView();
    }

    @Override
    public void attachView(ScheduleView view) {
        mScheduleView = view;
        mScheduleView.showLessonList(mLessonList);
    }

    @Override
    public void detachView() {
        mScheduleView = null;
    }

    private void onSuccess(List<Lesson> lessonList) {
        mLessonList = lessonList;
        showFilteredLessonList();
    }

    private void onError(Throwable throwable) {
        if (isViewAttached()) {
            mScheduleView.showError(throwable);
        }
    }

    private boolean isViewAttached() {
        return mScheduleView != null;
    }

    private List<Lesson> transformScheduleToLesson(ScheduleModel model) {
        List<Lesson> lessonList = new ArrayList<>(model.scheduleList.size());
        for (Schedule schedule : model.scheduleList) {
            lessonList.add(new Lesson(schedule, model.weekDay));
        }
        return lessonList;
    }
}
