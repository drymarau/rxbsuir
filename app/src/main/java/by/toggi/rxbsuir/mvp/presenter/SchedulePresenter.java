package by.toggi.rxbsuir.mvp.presenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import by.toggi.rxbsuir.db.RxBsuirOpenHelper;
import by.toggi.rxbsuir.db.model.Lesson;
import by.toggi.rxbsuir.mvp.Presenter;
import by.toggi.rxbsuir.mvp.view.ScheduleView;
import by.toggi.rxbsuir.rest.BsuirService;
import by.toggi.rxbsuir.rest.model.Schedule;
import by.toggi.rxbsuir.rest.model.ScheduleModel;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SchedulePresenter implements Presenter<ScheduleView> {

    private ScheduleView mScheduleView;
    private BsuirService mService;
    private RxBsuirOpenHelper mOpenHelper;
    private List<Lesson> mLessonList = new ArrayList<>();

    @Inject
    public SchedulePresenter(BsuirService service, RxBsuirOpenHelper openHelper) {
        mService = service;
        mOpenHelper = openHelper;
    }

    @Override
    public void onCreate() {
        mService.getGroupSchedule(111801).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .flatMap(scheduleXmlModels -> Observable.from(scheduleXmlModels.scheduleModelList))
                .flatMap(scheduleModel -> Observable.from(transformScheduleToLesson(scheduleModel)))
                .toList()
                .subscribe(this::onSuccess, this::onError);
    }

    @Override
    public void onDestroy() {
        detachView();
    }

    @Override
    public void attachView(ScheduleView scheduleView) {
        if (scheduleView == null) {
            throw new NullPointerException("ScheduleView is null!");
        }
        mScheduleView = scheduleView;
    }

    @Override
    public void detachView() {
        mScheduleView = null;
    }

    private void onSuccess(List<Lesson> lessonList) {
        mLessonList = lessonList;
    }

    private void onError(Throwable throwable) {
        if (isViewAttached()) {
            mScheduleView.showError(throwable);
        }
    }

    private List<Lesson> transformScheduleToLesson(ScheduleModel model) {
        List<Lesson> lessonList = new ArrayList<>(model.scheduleList.size());
        for (Schedule schedule : model.scheduleList) {
            lessonList.add(new Lesson(schedule, model.weekDay));
        }
        return lessonList;
    }

    private boolean isViewAttached() {
        return mScheduleView != null;
    }
}
