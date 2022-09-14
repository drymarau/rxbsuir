package by.toggi.rxbsuir.mvp.presenter;

import android.support.annotation.Nullable;

import javax.inject.Inject;

import by.toggi.rxbsuir.db.model.Lesson;
import by.toggi.rxbsuir.mvp.Presenter;
import by.toggi.rxbsuir.mvp.view.LessonDetailView;

public class LessonDetailPresenter extends Presenter<LessonDetailView> {

    private Lesson mLesson;

    @Inject
    public LessonDetailPresenter() {
    }

    public String getLessonNote() {
        return mLesson.getNote();
    }

    public void setLessonNote(@Nullable String lessonNote) {
        mLesson.setNote(lessonNote);
    }

    public void setLesson(Lesson lesson) {
        mLesson = lesson;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
        detachView();
    }
}
