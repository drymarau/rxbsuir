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

    @Inject
    public SchedulePresenter(BsuirService service) {
        mService = service;
    }

    @Override
    public void onCreate() {
        mService.getGroupSchedule(211801).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .flatMap(scheduleXmlModels -> Observable.from(scheduleXmlModels.scheduleModelList))
                .flatMap(scheduleModel -> Observable.from(scheduleModel.scheduleList))
                .filter(schedule -> schedule.weekNumberList.contains(1))
                .toList()
                .subscribe(scheduleList -> {
                    mScheduleList = scheduleList;
                    if (isViewAttached()) {
                        mScheduleView.showScheduleList(mScheduleList);
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
