package by.toggi.rxbsuir.mvp;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import by.toggi.rxbsuir.model.Schedule;
import by.toggi.rxbsuir.model.ScheduleXmlModels;
import by.toggi.rxbsuir.rest.BsuirService;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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
        if (isViewAttached()) {
            mService.getGroupSchedule(211801, new Callback<ScheduleXmlModels>() {
                @Override
                public void success(ScheduleXmlModels scheduleXmlModels, Response response) {
                    mScheduleList = scheduleXmlModels.scheduleModelList.get(0).scheduleList;
                    mScheduleView.showScheduleList(mScheduleList);
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }
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
