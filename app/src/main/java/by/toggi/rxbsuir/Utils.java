package by.toggi.rxbsuir;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Utils class with all RxBsuir goodies.
 */
public class Utils {

    private Utils() {
    }

    /**
     * Opens play store page or shows a toast if play services are missing.
     *
     * @param context the context
     */
    public static void openPlayStorePage(Context context) {
        try {
            context.startActivity(new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)
            ));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, R.string.play_store_not_found, Toast.LENGTH_SHORT).show();
        }
    }

    public static void openPrivacyPolicyPage(Context context) {
        try {
            var uri = Uri.parse("https://drymarev.github.io/rxbsuir/docs/privacy_policy.html");
            var intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        } catch (ActivityNotFoundException ignored) {
        }
    }

    /**
     * Checks for network connection.
     *
     * @param context the context
     * @return the boolean
     */
    public static boolean hasNetworkConnection(Context context) {
        var manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        var activeNetworkInfo = manager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
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
        var weeks = ChronoUnit.WEEKS.between(getStartYear(), localDate);
        return (int) weeks % 4 + 1;
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

    /**
     * RxJava composite subscription unsubscribe helper.
     *
     * @param compositeSubscription {@link CompositeSubscription}
     */
    public static void unsubscribeComposite(CompositeSubscription compositeSubscription) {
        if (compositeSubscription != null && !compositeSubscription.isUnsubscribed() && compositeSubscription.hasSubscriptions()) {
            compositeSubscription.unsubscribe();
        }
    }

    private static LocalDate getStartYear() {
        var localDate = LocalDate.now();
        if (localDate.getMonth().compareTo(Month.SEPTEMBER) < 0) {
            return LocalDate.of(localDate.getYear() - 1, Month.SEPTEMBER, 1).with(DayOfWeek.MONDAY);
        }
        return LocalDate.of(localDate.getYear(), Month.SEPTEMBER, 1).with(DayOfWeek.MONDAY);
    }
}
