package by.toggi.rxbsuir;

import android.support.annotation.NonNull;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;
import org.threeten.bp.temporal.ChronoUnit;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import timber.log.Timber;

/**
 * Utils class with all RxBsuir goodies.
 */
public class Utils {

    private Utils() {
    }

    /**
     * Gets date list.
     *
     * @param startDate the start date
     * @param endDate the end date
     * @return the date list
     */
    public static List<LocalDate> getDateList(@NonNull String startDate, @NonNull String endDate) {
        List<LocalDate> dateList = new ArrayList<>();
        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);
        while (!start.isAfter(end)) {
            if (start.getDayOfWeek() != DayOfWeek.SUNDAY) {
                dateList.add(start);
            }
            start = start.plusDays(1);
        }
        Timber.d(dateList.toString());
        return dateList;
    }

    /**
     * Gets current week number.
     *
     * @return the current week number
     */
    public static int getCurrentWeekNumber() {
        Long weeks = ChronoUnit.WEEKS.between(getStartYear(), LocalDate.now());
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

}
