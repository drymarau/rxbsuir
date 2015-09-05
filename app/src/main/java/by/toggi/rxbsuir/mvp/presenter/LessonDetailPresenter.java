package by.toggi.rxbsuir.mvp.presenter;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import by.toggi.rxbsuir.activity.LessonActivity.DetailItem;
import by.toggi.rxbsuir.db.model.Lesson;
import by.toggi.rxbsuir.mvp.Presenter;
import by.toggi.rxbsuir.mvp.view.LessonDetailView;
import by.toggi.rxbsuir.rest.model.Employee;

public class LessonDetailPresenter extends Presenter<LessonDetailView> {

    private final StorIOSQLite mStorIOSQLite;
    private Lesson mLesson;

    @Inject
    public LessonDetailPresenter(StorIOSQLite storIOSQLite) {
        mStorIOSQLite = storIOSQLite;
    }

    public void setLesson(Lesson lesson) {
        mLesson = lesson;
        if (isViewAttached()) {
            getView().showLessonDetail(getDetailItemList(lesson));
        }
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {
        detachView();
    }

    private List<DetailItem> getDetailItemList(Lesson lesson) {
        List<DetailItem> detailItemList = new ArrayList<>();
        if (lesson.getNote() != null && !lesson.getNote().isEmpty()) {
            detailItemList.add(DetailItem.newInstance(
                    DetailItem.Type.NOTE,
                    lesson.getNote(),
                    true
            ));
        }
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
        return detailItemList;
    }
}
