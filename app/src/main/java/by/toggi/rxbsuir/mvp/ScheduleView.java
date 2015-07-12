package by.toggi.rxbsuir.mvp;

import java.util.List;

import by.toggi.rxbsuir.db.model.Lesson;

public interface ScheduleView extends View {

    void showLessonList(List<Lesson> lessonList);

}
