package by.toggi.rxbsuir.db.model;

import org.threeten.bp.LocalDate;

public class LessonWithDate {

    private LocalDate localDate;
    private Lesson lesson;

    private LessonWithDate() {
    }

    /**
     * New instance.
     *
     * @param lesson the lesson
     * @param localDate the local date
     * @return the lesson with date
     */
    public static LessonWithDate newInstance(Lesson lesson, LocalDate localDate) {
        LessonWithDate lessonWithDate = new LessonWithDate();
        lessonWithDate.lesson = lesson;
        lessonWithDate.localDate = localDate;
        return lessonWithDate;
    }

}
