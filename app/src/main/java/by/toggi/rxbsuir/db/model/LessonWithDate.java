package by.toggi.rxbsuir.db.model;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

public class LessonWithDate {

    private LocalDate localDate;
    private Lesson lesson;

    private LessonWithDate() {
    }

    /**
     * New instance.
     *
     * @param lesson    the lesson
     * @param localDate the local date
     * @return the lesson with date
     */
    public static LessonWithDate newInstance(Lesson lesson, LocalDate localDate) {
        LessonWithDate lessonWithDate = new LessonWithDate();
        lessonWithDate.lesson = lesson;
        lessonWithDate.localDate = localDate;
        return lessonWithDate;
    }

    /**
     * Gets pretty date.
     *
     * @return the pretty date
     */
    public String getPrettyDate() {
        return localDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * Gets lesson.
     *
     * @return the lesson
     */
    public Lesson getLesson() {
        return lesson;
    }

}
