package by.toggi.rxbsuir.mvp.presenter;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import by.toggi.rxbsuir.db.RxBsuirContract;
import by.toggi.rxbsuir.db.model.Lesson;
import by.toggi.rxbsuir.mvp.Presenter;
import by.toggi.rxbsuir.mvp.view.ScheduleView;
import by.toggi.rxbsuir.rest.BsuirService;
import by.toggi.rxbsuir.rest.model.Schedule;
import by.toggi.rxbsuir.rest.model.ScheduleModel;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * The type Schedule presenter.
 */
public class SchedulePresenter implements Presenter<ScheduleView> {

    private ScheduleView mScheduleView;
    private BsuirService mService;
    private StorIOSQLite mStorIOSQLite;
    private String mGroupNumber;

    /**
     * Instantiates a new Schedule presenter.
     *
     * @param service      the service
     * @param storIOSQLite the stor iOSQ lite
     */
    @Inject
    public SchedulePresenter(BsuirService service, StorIOSQLite storIOSQLite) {
        mService = service;
        mStorIOSQLite = storIOSQLite;
    }

    /**
     * Sets group number and updates schedule list.
     *
     * @param groupNumber the group number
     */
    public void setGroupNumber(String groupNumber) {
        mGroupNumber = groupNumber;
        getStudentGroupSchedule();
    }

    /**
     * Gets student group schedule.
     */
    // TODO Implement schedule storage and retrieval from SQLite
    public void getStudentGroupSchedule() {
        mService.getGroupSchedule(mGroupNumber).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .flatMap(scheduleXmlModels -> Observable.from(scheduleXmlModels.scheduleModelList))
                .flatMap(scheduleModel -> Observable.from(transformScheduleToLesson(scheduleModel)))
                .toList()
                .subscribe(this::onNetworkSuccess, this::onNetworkError);
    }

    @Override
    public void onCreate() {
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

    private void onNetworkSuccess(List<Lesson> lessonList) {
        Observable.concat(
                mStorIOSQLite.delete()
                        .byQuery(DeleteQuery.builder().table(RxBsuirContract.LessonEntry.TABLE_NAME).build())
                        .prepare()
                        .createObservable(),
                mStorIOSQLite.put()
                        .objects(lessonList)
                        .prepare()
                        .createObservable()
        ).subscribe();
    }

    private void onNetworkError(Throwable throwable) {
    }

    private List<Lesson> transformScheduleToLesson(ScheduleModel model) {
        List<Lesson> lessonList = new ArrayList<>(model.scheduleList.size());
        for (Schedule schedule : model.scheduleList) {
            lessonList.add(new Lesson(null, schedule, model.weekDay));
        }
        return lessonList;
    }

    private boolean isViewAttached() {
        return mScheduleView != null;
    }
}
