package by.toggi.rxbsuir.mvp.presenter;

import android.support.annotation.Nullable;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import by.toggi.rxbsuir.Utils;
import by.toggi.rxbsuir.db.RxBsuirContract;
import by.toggi.rxbsuir.db.model.Lesson;
import by.toggi.rxbsuir.db.model.LessonWithDate;
import by.toggi.rxbsuir.mvp.Presenter;
import by.toggi.rxbsuir.mvp.view.TermView;
import by.toggi.rxbsuir.rest.AcademicCalendarService;
import by.toggi.rxbsuir.rest.model.AcademicCalendar;
import by.toggi.rxbsuir.rest.model.AcademicCalendarResponse;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

import static by.toggi.rxbsuir.activity.ScheduleActivity.KEY_IS_GROUP_SCHEDULE;
import static by.toggi.rxbsuir.activity.ScheduleActivity.KEY_SYNC_ID;
import static by.toggi.rxbsuir.db.RxBsuirContract.LessonEntry;

public class TermPresenter extends Presenter<TermView> {

    private final StorIOSQLite mStorIOSQLite;
    private Observable<List<Lesson>> mScheduleObservable;
    private int mSubgroupNumber = 0;
    private String mSyncId;
    private Subscription mSubscription;
    private List<AcademicCalendar> mAcademicCalendarList;

    @Inject
    public TermPresenter(@Named(KEY_IS_GROUP_SCHEDULE) boolean isGroupSchedule, @Nullable @Named(KEY_SYNC_ID) String syncId, AcademicCalendarService service, StorIOSQLite storIOSQLite) {
        mStorIOSQLite = storIOSQLite;
        mSyncId = syncId;
        if (isGroupSchedule) {
            mScheduleObservable = getGroupScheduleObservable(mSyncId, mSubgroupNumber);
        } else {
            mScheduleObservable = getEmployeeListObservable(mSyncId, mSubgroupNumber);
        }

        Observable.concat(
                getAcademicCalendarDbObservable(),
                getAcademicCalendarNetworkWithCacheObservable(service)
        ).first(academicCalendars -> !academicCalendars.isEmpty())
                .subscribe(academicCalendars -> {
                    mAcademicCalendarList = academicCalendars;
                });

    }

    /**
     * Sets group number and updates the list.
     *
     * @param syncId the group number
     */
    public void setSyncId(String syncId, boolean isGroupSchedule) {
        mSyncId = syncId;
        if (isGroupSchedule) {
            mScheduleObservable = getGroupScheduleObservable(mSyncId, mSubgroupNumber);
        } else {
            mScheduleObservable = getEmployeeListObservable(mSyncId, mSubgroupNumber);
        }
        onCreate();
    }

    /**
     * Sets subgroup number.
     *
     * @param subgroup1 the subgroup 1 state
     * @param subgroup2 the subgroup 2 state
     */
    public void setSubgroupNumber(boolean subgroup1, boolean subgroup2, boolean isGroupSchedule) {
        if (subgroup1 && subgroup2) {
            mSubgroupNumber = 0;
        } else if (!subgroup1 && !subgroup2) {
            mSubgroupNumber = 3;
        } else {
            if (subgroup1) {
                mSubgroupNumber = 1;
            } else {
                mSubgroupNumber = 2;
            }
        }
        if (isGroupSchedule) {
            mScheduleObservable = getGroupScheduleObservable(mSyncId, mSubgroupNumber);
        } else {
            mScheduleObservable = getEmployeeListObservable(mSyncId, mSubgroupNumber);
        }
        onCreate();
    }

    @Override
    public void onCreate() {
        Utils.unsubscribe(mSubscription);
        mSubscription = mScheduleObservable.subscribe(lessons -> {
            if (lessons.size() > 0) {
                showLessonList(getLessonWithDateList(lessons));
            }
        });
    }

    @Override
    public void onDestroy() {
        Utils.unsubscribe(mSubscription);
        detachView();
    }

    private Observable<List<Lesson>> getGroupScheduleObservable(@Nullable String syncId, int subgroupNumber) {
        return syncId == null ? Observable.empty() : mStorIOSQLite.get()
                .listOfObjects(Lesson.class)
                .withQuery(Query.builder()
                        .table(LessonEntry.TABLE_NAME)
                        .where(LessonEntry.filterByGroupAndSubgroup(syncId, subgroupNumber))
                        .build())
                .prepare()
                .createObservable()
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<List<Lesson>> getEmployeeListObservable(@Nullable String syncId, int subgroupNumber) {
        return syncId == null ? Observable.empty() : mStorIOSQLite.get()
                .listOfObjects(Lesson.class)
                .withQuery(Query.builder()
                        .table(LessonEntry.TABLE_NAME)
                        .where(LessonEntry.filterByEmployeeAndSubgroup(syncId, subgroupNumber))
                        .build())
                .prepare()
                .createObservable()
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void showLessonList(List<LessonWithDate> lessonWithDateList) {
        if (isViewAttached()) {
            getView().showLessonList(lessonWithDateList);
        }
    }

    private Observable<List<AcademicCalendar>> getAcademicCalendarDbObservable() {
        return mStorIOSQLite.get().listOfObjects(AcademicCalendar.class)
                .withQuery(Query.builder()
                        .table(RxBsuirContract.AcademicCalendarEntry.TABLE_NAME).build())
                .prepare()
                .createObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .take(1);
    }

    private Observable<List<AcademicCalendar>> getAcademicCalendarNetworkWithCacheObservable(AcademicCalendarService service) {
        return service.getAcademicCalendar()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(AcademicCalendarResponse::getAcademicCalendarList)
                .doOnNext(academicCalendars -> mStorIOSQLite.put().objects(academicCalendars).prepare().executeAsBlocking());

    }

    private List<LessonWithDate> getLessonWithDateList(List<Lesson> lessonList) {
        List<LessonWithDate> lessonWithDateList = new ArrayList<>();
        for (LocalDate date : Utils.getDateList("2015-09-01", "2015-12-28")) {
            for (Lesson lesson : lessonList) {
                if (filter(date.getDayOfWeek(), Utils.getWeekNumber(date), lesson)) {
                    LessonWithDate lessonWithDate = LessonWithDate.newInstance(lesson, date);
                    Timber.d(lessonWithDate.toString());
                    lessonWithDateList.add(lessonWithDate);
                }
            }
        }
        return lessonWithDateList;
    }

    private boolean filter(DayOfWeek dayOfWeek, int weekNumber, Lesson lesson) {
        return lesson.getWeekday() == dayOfWeek && lesson.getWeekNumberList().contains(weekNumber);
    }
}
