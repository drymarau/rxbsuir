package by.toggi.rxbsuir.mvp.view;

import java.util.List;

import by.toggi.rxbsuir.db.model.LessonWithDate;
import by.toggi.rxbsuir.mvp.View;

public interface TermView extends View {

    void showLessonList(List<LessonWithDate> lessonWithDateList);

}
