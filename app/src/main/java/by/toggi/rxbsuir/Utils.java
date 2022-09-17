package by.toggi.rxbsuir;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.widget.Toast;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;

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

    private static LocalDate getStartYear() {
        var localDate = LocalDate.now();
        if (localDate.getMonth().compareTo(Month.SEPTEMBER) < 0) {
            return LocalDate.of(localDate.getYear() - 1, Month.SEPTEMBER, 1).with(DayOfWeek.MONDAY);
        }
        return LocalDate.of(localDate.getYear(), Month.SEPTEMBER, 1).with(DayOfWeek.MONDAY);
    }
}
