package by.toggi.rxbsuir.mvp.presenter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import by.toggi.rxbsuir.Utils;
import by.toggi.rxbsuir.db.model.Lesson;
import by.toggi.rxbsuir.mvp.Presenter;
import by.toggi.rxbsuir.mvp.view.ScheduleView;
import by.toggi.rxbsuir.rest.BsuirService;
import by.toggi.rxbsuir.rest.model.Employee;
import by.toggi.rxbsuir.rest.model.Schedule;
import by.toggi.rxbsuir.rest.model.ScheduleJsonModels;
import by.toggi.rxbsuir.rest.model.ScheduleModel;
import by.toggi.rxbsuir.rest.model.StudentGroup;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * The type Schedule presenter.
 */
@Singleton
public class SchedulePresenter extends Presenter<ScheduleView> {

    private final BsuirService mService;
    private String mSyncId;
    private boolean mIsGroupSchedule;

    /**
     * Instantiates a new Schedule presenter.
     *
     * @param service the bsuirService
     */
    @Inject
    public SchedulePresenter(BsuirService service) {
        mService = service;
    }

    /**
     * Sets group number and updates schedule list.
     *
     * @param syncId the group number
     */
    public void setSyncId(@Nullable String syncId, Boolean isGroupSchedule) {
        if (syncId != null) {
            // Set syncId and isGroupSchedule
            mSyncId = syncId;
            mIsGroupSchedule = isGroupSchedule;
            // View.showLoading()
            if (isViewAttached()) {
                getView().showLoading();
            }
            getLessonListFromNetworkObservable(syncId, isGroupSchedule)
                    .first(lessonList -> !lessonList.isEmpty())
                    .subscribe(lessonList -> {
                        // View.showContent()
                        if (isViewAttached()) {
                            getView().showContent();
                        }
                    }, this::onError);
        }
    }

    /**
     * Retry network request with the same group or employee.
     */
    public void retry() {
        // View.showLoading()
        if (isViewAttached()) {
            getView().showLoading();
        }
        // Make a network request with current syncId and isGroupSchedule
        getLessonListFromNetworkObservable(mSyncId, mIsGroupSchedule).subscribe(lessonList -> {
            if (isViewAttached()) {
                getView().showContent();
            }
        }, this::onError);
        // View.showContent()

    }


    /**
     * Remove current syncId from db.
     */
    public void remove(String syncId, Boolean isGroupSchedule) {
        // Remove all records from db with supplied syncId;
        var o = isGroupSchedule ? getCacheGroupObservable(false) : getCacheEmployeeObservable(false);
        o.doOnError(this::onError).subscribe();
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
        detachView();
    }

    private void onNetworkSuccess(List<Lesson> lessonList, boolean isGroupSchedule) {
        var o = isGroupSchedule ? getCacheGroupObservable(true) : getCacheEmployeeObservable(true);
        o.subscribe();
    }

    private Observable<Employee> getCacheEmployeeObservable(boolean isCached) {
        return Observable.never();
    }

    private Observable<StudentGroup> getCacheGroupObservable(boolean isCached) {
        return Observable.never();
    }

    private void onError(Throwable throwable) {
        Timber.e(throwable, "Something went wrong.");
        if (isViewAttached()) {
            if (throwable.getMessage() != null && throwable.getMessage().contains("org.simpleframework.xml.core.ValueRequiredException")) {
                getView().showError(Error.EMPTY_SCHEDULE);
            } else {
                getView().showError(Error.NETWORK);
            }
        }
    }

    private List<Lesson> transformScheduleToLesson(ScheduleModel model, boolean isGroupSchedule) {
        List<Lesson> lessonList = new ArrayList<>(model.schedule.size());
        for (Schedule schedule : model.schedule) {
            lessonList.add(new Lesson(mSyncId, schedule, Utils.convertWeekdayToDayOfWeek(model.weekDay), isGroupSchedule));
        }
        return lessonList;
    }

    private Observable<List<Lesson>> getLessonListFromNetworkObservable(@NonNull String syncId, boolean isGroupSchedule) {
        Observable<ScheduleJsonModels> scheduleXmlModelsObservable = isGroupSchedule
                ? mService.getGroupSchedule(syncId.replace("М", "M"))
                : mService.getEmployeeSchedule(syncId);
        return scheduleXmlModelsObservable.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
                .flatMap(scheduleJsonModels -> Observable.from(scheduleJsonModels.schedules))
                .flatMap(scheduleModel -> Observable.from(transformScheduleToLesson(scheduleModel, isGroupSchedule)))
                .toList()
                .doOnNext(lessonList -> onNetworkSuccess(lessonList, isGroupSchedule));
    }

    public enum Error {
        NETWORK, EMPTY_SCHEDULE
    }
}
