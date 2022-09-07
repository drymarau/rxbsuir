package by.toggi.rxbsuir;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

import by.toggi.rxbsuir.db.model.Lesson;
import by.toggi.rxbsuir.receiver.AlarmReceiver;
import by.toggi.rxbsuir.receiver.AppWidgetScheduleProvider;

/**
 * Static class with different intent helpers which don't really go into any other class.
 */
public class IntentUtils {

    private static final int REQUEST_CODE_WIDGET_UPDATE = 34563;
    private static final int REQUEST_CODE_LESSON_REMINDER = 15613;

    private IntentUtils() {
    }

    /**
     * Gets day schedule share intent.
     *
     * @param lessonList the lesson list
     * @param title      the title
     * @param date       the date
     * @return the day schedule share intent
     */
    public static Intent getDayScheduleShareIntent(List<Lesson> lessonList, String title, LocalDate date) {
        var builder = new StringBuilder(title).append(" (")
                .append(date.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)))
                .append(")");
        for (int i = 0, size = lessonList.size(); i < size; i++) {
            builder.append("\n").append(lessonList.get(i).getPrettyLesson());
        }

        return new Intent(Intent.ACTION_SEND).setType("text/plain")
                .putExtra(Intent.EXTRA_TEXT, builder.toString());
    }

    /**
     * Gets widget update pending intent.
     *
     * @param context the context
     * @return the widget update pending intent
     */
    public static PendingIntent getWidgetUpdatePendingIntent(Context context) {
        return PendingIntent.getBroadcast(
                context,
                REQUEST_CODE_WIDGET_UPDATE,
                new Intent(context, AppWidgetScheduleProvider.class)
                        .setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
                        .putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, Utils.getAppWidgetIds(context)),
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }

    public static PendingIntent getNotificationPendingIntent(Context context) {
        return PendingIntent.getBroadcast(
                context,
                REQUEST_CODE_LESSON_REMINDER,
                new Intent(context, AlarmReceiver.class),
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }

}
