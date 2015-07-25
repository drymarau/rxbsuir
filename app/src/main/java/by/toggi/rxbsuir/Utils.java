package by.toggi.rxbsuir;

import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;
import org.threeten.bp.temporal.ChronoUnit;

public class Utils {

    private Utils() {
    }

    public static int getCurrentWeekNumber() {
        Long weeks = ChronoUnit.WEEKS.between(getStartYear(), LocalDate.now());
        return weeks.intValue() % 4 + 1;
    }

    private static LocalDate getStartYear() {
        LocalDate localDate = LocalDate.now();
        if (localDate.getMonth().compareTo(Month.SEPTEMBER) < 0) {
            return LocalDate.of(localDate.getYear() - 1, Month.SEPTEMBER, 1);
        }
        return LocalDate.of(localDate.getYear(), Month.SEPTEMBER, 1);
    }

}
