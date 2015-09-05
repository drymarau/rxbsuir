package by.toggi.rxbsuir.mvp.view;

import java.util.List;

import by.toggi.rxbsuir.activity.LessonActivity;
import by.toggi.rxbsuir.mvp.View;

public interface LessonDetailView extends View {

    void showLessonDetail(List<LessonActivity.DetailItem> detailItemList);

}
