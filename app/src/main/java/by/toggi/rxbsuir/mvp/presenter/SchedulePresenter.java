package by.toggi.rxbsuir.mvp.presenter;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

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
import by.toggi.rxbsuir.rest.model.StudentGroup;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SchedulePresenter implements Presenter<ScheduleView> {

    private ScheduleView mScheduleView;
    private BsuirService mService;
    private StorIOSQLite mStorIOSQLite;

    @Inject
    public SchedulePresenter(BsuirService service, StorIOSQLite storIOSQLite) {
        mService = service;
        mStorIOSQLite = storIOSQLite;
    }

    @Override
    public void onCreate() {
        if (isViewAttached()) {
            mScheduleView.showLoading();
        }
        Observable<List<StudentGroup>> disk = mStorIOSQLite.get()
                .listOfObjects(StudentGroup.class)
                .withQuery(Query.builder()
                        .table(RxBsuirContract.StudentGroupEntry.TABLE_NAME)
                        .build())
                .prepare()
                .createObservable()
                .observeOn(AndroidSchedulers.mainThread());
        disk.subscribe(studentGroups -> {
            if (studentGroups == null || studentGroups.isEmpty()) {
                getStudentGroupsFromNetwork();
            } else {
                updateStudentGroupListInView(studentGroups);
            }
        });
//        mService.getGroupSchedule(111801).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
//                .flatMap(scheduleXmlModels -> Observable.from(scheduleXmlModels.scheduleModelList))
//                .flatMap(scheduleModel -> Observable.from(transformScheduleToLesson(scheduleModel)))
//                .toList()
//                .subscribe(this::onSuccess, this::onError);
    }

    private void updateStudentGroupListInView(List<StudentGroup> studentGroupList) {
        Observable.from(studentGroupList)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .map(StudentGroup::toString)
                .toList()
                .subscribe(strings -> {
                    if (isViewAttached()) {
                        mScheduleView.updateStudentGroupList(strings);
                    }
                });
    }

    private void getStudentGroupsFromNetwork() {
        Observable<List<StudentGroup>> network = mService.getStudentGroups()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .map(studentGroupXmlModels -> studentGroupXmlModels.studentGroupList);
        network.subscribe(this::saveStudentGroupsToDisk);
    }

    private void saveStudentGroupsToDisk(List<StudentGroup> studentGroupList) {
        mStorIOSQLite.put()
                .objects(studentGroupList)
                .prepare()
                .createObservable()
                .subscribe();
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
        if (isViewAttached()) {
            mScheduleView.finishRefresh();
        }
    }

    private void onError(Throwable throwable) {
        if (isViewAttached()) {
            mScheduleView.showError(throwable);
            mScheduleView.finishRefresh();
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
