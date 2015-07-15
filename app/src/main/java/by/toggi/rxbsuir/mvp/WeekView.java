package by.toggi.rxbsuir.mvp;

import java.util.List;

import by.toggi.rxbsuir.db.model.Lesson;

public interface WeekView extends View {

    void showLessonList(List<Lesson> lessonList);

    void showLoading();

    void showError(Throwable t);

}
