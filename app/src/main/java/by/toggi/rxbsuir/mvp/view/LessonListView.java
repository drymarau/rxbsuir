package by.toggi.rxbsuir.mvp.view;

import java.util.List;

import by.toggi.rxbsuir.model.Lesson;
import by.toggi.rxbsuir.mvp.View;

public interface LessonListView extends View {

    void showLessonList(List<Lesson> lessonList);

    void showEmptyState();

}
