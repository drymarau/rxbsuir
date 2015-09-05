package by.toggi.rxbsuir.mvp.presenter;

import android.support.annotation.Nullable;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import by.toggi.rxbsuir.Utils;
import by.toggi.rxbsuir.activity.LessonActivity.DetailItem;
import by.toggi.rxbsuir.db.model.Lesson;
import by.toggi.rxbsuir.mvp.Presenter;
import by.toggi.rxbsuir.mvp.view.LessonDetailView;
import by.toggi.rxbsuir.rest.model.Employee;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static by.toggi.rxbsuir.db.RxBsuirContract.LessonEntry;

public class LessonDetailPresenter extends Presenter<LessonDetailView> {

    private final StorIOSQLite mStorIOSQLite;
    private Lesson mLesson;
    private Subscription mSubscription;

    @Inject
    public LessonDetailPresenter(StorIOSQLite storIOSQLite) {
        mStorIOSQLite = storIOSQLite;
    }

    public String getLessonNote() {
        return mLesson.getNote();
    }

    public void setLessonNote(@Nullable String lessonNote) {
        mLesson.setNote(lessonNote);
        mStorIOSQLite.put()
                .object(mLesson)
                .prepare()
                .createObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void setLesson(Lesson lesson) {
        mLesson = lesson;
        mSubscription = mStorIOSQLite.get()
                .listOfObjects(Lesson.class)
                .withQuery(Query.builder()
                        .table(LessonEntry.TABLE_NAME)
                        .where(LessonEntry._ID + "= '" + mLesson.getId() + "'")
                        .build())
                .prepare()
                .createObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lessonList -> {
                    if (lessonList.size() == 1) {
                        if (isViewAttached()) {
                            getView().showLessonDetail(getDetailItemList(lessonList.get(0)));
                        }
                    }
                });

    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {
        Utils.unsubscribe(mSubscription);
        detachView();
    }

    private List<DetailItem> getDetailItemList(Lesson lesson) {
        List<DetailItem> detailItemList = new ArrayList<>();
        detailItemList.add(DetailItem.newInstance(
                DetailItem.Type.TIME,
                lesson.getPrettyLessonTimeStart() + '-' + lesson.getPrettyLessonTimeEnd(),
                true
        ));
        detailItemList.add(DetailItem.newInstance(
                DetailItem.Type.WEEKDAY,
                lesson.getPrettyWeekday(),
                lesson.getPrettyWeekNumberList(),
                true
        ));
        if (lesson.getAuditoryList() != null && !lesson.getAuditoryList().isEmpty()) {
            detailItemList.add(DetailItem.newInstance(
                    DetailItem.Type.AUDITORY,
                    lesson.getPrettyAuditoryList(),
                    true
            ));
        }
        if (lesson.isGroupSchedule()) {
            for (int i = 0, size = lesson.getEmployeeList().size(); i < size; i++) {
                Employee employee = lesson.getEmployeeList().get(i);
                detailItemList.add(DetailItem.newInstance(
                        DetailItem.Type.EMPLOYEE,
                        employee.toString(),
                        i == 0
                ));
            }
        } else {
            for (int i = 0, size = lesson.getStudentGroupList().size(); i < size; i++) {
                String group = lesson.getStudentGroupList().get(i);
                detailItemList.add(DetailItem.newInstance(
                        DetailItem.Type.GROUP,
                        group,
                        i == 0
                ));
            }
        }
        if (lesson.getNote() != null && !lesson.getNote().isEmpty()) {
            detailItemList.add(DetailItem.newInstance(
                    DetailItem.Type.NOTE,
                    lesson.getNote(),
                    true
            ));
        }
        return detailItemList;
    }
}
