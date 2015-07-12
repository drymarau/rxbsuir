package by.toggi.rxbsuir.mvp;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import by.toggi.rxbsuir.model.Schedule;
import by.toggi.rxbsuir.rest.BsuirService;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SchedulePresenter implements Presenter {

    private List<Schedule> mScheduleList = new ArrayList<>();
    private ScheduleView mScheduleView;
    private BsuirService mService;
    private int mWeekNumber;

    @Inject
    public SchedulePresenter(BsuirService service) {
        mService = service;
        mWeekNumber = 1;
    }

    public void setWeekNumber(int weekNumber) {
        mWeekNumber = weekNumber;
        showFilteredScheduleList();
    }

    @Override
    public void onCreate() {
        mService.getGroupSchedule(111801).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .flatMap(scheduleXmlModels -> Observable.from(scheduleXmlModels.scheduleModelList))
                .flatMap(scheduleModel -> Observable.from(scheduleModel.scheduleList))
                .toList()
                .subscribe(scheduleList -> {
                    mScheduleList = scheduleList;
                    showFilteredScheduleList();
                });
    }

    private void showFilteredScheduleList() {
        Observable.from(mScheduleList).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.computation())
                .filter(schedule -> schedule.weekNumberList.contains(mWeekNumber))
                .toList()
                .subscribe(scheduleList -> {
                    if (isViewAttached()) {
                        mScheduleView.showScheduleList(scheduleList);
                    }
                });
    }

    @Override
    public void onDestroy() {
        detachView();
    }

    @Override
    public void attachView(View view) {
        mScheduleView = (ScheduleView) view;
        mScheduleView.showScheduleList(mScheduleList);
    }

    @Override
    public void detachView() {
        mScheduleView = null;
    }

    private boolean isViewAttached() {
        return mScheduleView != null;
    }
}
