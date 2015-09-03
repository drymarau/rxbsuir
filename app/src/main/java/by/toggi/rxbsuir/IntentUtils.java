package by.toggi.rxbsuir;


import android.content.Intent;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.FormatStyle;

import java.util.List;

import by.toggi.rxbsuir.db.model.Lesson;

/**
 * Static class with different intent helpers which don't really go into any other class.
 */
public class IntentUtils {

    private IntentUtils() {
    }

    public static Intent getDayScheduleShareIntent(List<Lesson> lessonList, String title, LocalDate date) {
        StringBuilder builder = new StringBuilder(title).append(" (")
                .append(date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)))
                .append(")");
        for (int i = 0, size = lessonList.size(); i < size; i++) {
            builder.append("\n").append(lessonList.get(i).getPrettyLesson());
        }

        return new Intent(Intent.ACTION_SEND).setType("text/plain")
                .putExtra(Intent.EXTRA_TEXT, builder.toString());
    }

}
