package by.toggi.rxbsuir;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.IntentCompat;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.LocalTime;
import org.threeten.bp.Month;
import org.threeten.bp.ZoneId;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.concurrent.TimeUnit;

import by.toggi.rxbsuir.activity.WeekScheduleActivity;
import by.toggi.rxbsuir.receiver.AlarmReceiver;
import rx.Subscription;

/**
 * Utils class with all RxBsuir goodies.
 */
public class Utils {

    public static final int REQUEST_CODE_LESSON_REMINDER = 15613;

    private Utils() {
    }

    /**
     * Sets alarm.
     *
     * @param context   the context
     * @param localTime the local time
     */
    public static void setAlarm(Context context, LocalTime localTime) {
        PendingIntent pendingIntent = getLessonReminderPendingIntent(context);

        LocalDateTime dateTime;
        if (LocalTime.now().isAfter(localTime)) {
            dateTime = LocalDateTime.of(LocalDate.now().plusDays(1), localTime);
        } else {
            dateTime = LocalDateTime.of(LocalDate.now(), localTime);
        }
        long triggerMillis = dateTime.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000;
        AlarmManager manager = getAlarmManager(context);
        manager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                triggerMillis,
                TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS),
                pendingIntent
        );
    }

    /**
     * Cancels alarm.
     *
     * @param context the context
     */
    public static void cancelAlarm(Context context) {
        PendingIntent intent = getLessonReminderPendingIntent(context);
        AlarmManager manager = getAlarmManager(context);
        manager.cancel(intent);
        intent.cancel();
    }

    /**
     * Restarts app.
     *
     * @param activity the activity
     */
    public static void restartApp(Activity activity) {
        activity.finish();
        ComponentName componentName = new ComponentName(activity, WeekScheduleActivity.class);
        final Intent intent = IntentCompat.makeMainActivity(componentName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    /**
     * Gets current week number.
     *
     * @return the current week number
     */
    public static int getCurrentWeekNumber() {
        return getWeekNumber(LocalDate.now());
    }

    /**
     * Gets week number.
     *
     * @param localDate the local date
     * @return the week number
     */
    public static int getWeekNumber(LocalDate localDate) {
        Long weeks = ChronoUnit.WEEKS.between(getStartYear(), localDate);
        return weeks.intValue() % 4 + 1;
    }

    /**
     * Convert weekday to {@link DayOfWeek}.
     *
     * @param weekday the weekday
     * @return the day of week
     */
    public static DayOfWeek convertWeekdayToDayOfWeek(@NonNull String weekday) {
        switch (weekday.toLowerCase()) {
            case "понедельник":
                return DayOfWeek.MONDAY;
            case "вторник":
                return DayOfWeek.TUESDAY;
            case "среда":
                return DayOfWeek.WEDNESDAY;
            case "четверг":
                return DayOfWeek.THURSDAY;
            case "пятница":
                return DayOfWeek.FRIDAY;
            case "суббота":
                return DayOfWeek.SATURDAY;
            case "воскресенье":
                return DayOfWeek.SUNDAY;
            default:
                throw new IllegalArgumentException("Unknown weekday: " + weekday);
        }
    }

    /**
     * RxJava unsubscribe helper.
     *
     * @param subscription the subscription
     */
    public static void unsubscribe(Subscription subscription) {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }

    private static LocalDate getStartYear() {
        LocalDate localDate = LocalDate.now();
        if (localDate.getMonth().compareTo(Month.SEPTEMBER) < 0) {
            return LocalDate.of(localDate.getYear() - 1, Month.SEPTEMBER, 1);
        }
        return LocalDate.of(localDate.getYear(), Month.SEPTEMBER, 1);
    }

    private static AlarmManager getAlarmManager(Context context) {
        return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    private static PendingIntent getLessonReminderPendingIntent(Context context) {
        return PendingIntent.getBroadcast(
                context,
                REQUEST_CODE_LESSON_REMINDER,
                new Intent(context, AlarmReceiver.class),
                PendingIntent.FLAG_UPDATE_CURRENT
        );
    }

}
