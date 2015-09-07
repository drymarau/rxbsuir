package by.toggi.rxbsuir;

import android.app.Application;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ApplicationTestCase;

import com.jakewharton.threetenabp.AndroidThreeTen;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.threeten.bp.LocalDate;
import org.threeten.bp.Month;
import org.threeten.bp.zone.ZoneRulesProvider;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(AndroidJUnit4.class)
public class UtilsTest extends ApplicationTestCase<Application> {
    public UtilsTest() {
        super(Application.class);
    }

    private final Application application =
            (Application) InstrumentationRegistry.getTargetContext().getApplicationContext();

    @Before
    public void setup() {
        AndroidThreeTen.init(application);
    }

    @Test
    public void litmus() {
        assertThat(ZoneRulesProvider.getAvailableZoneIds()).isNotEmpty();
    }

    @Test
    public void getWeekNumberTest() {
        assertThat(Utils.getWeekNumber(LocalDate.of(2015, Month.SEPTEMBER, 1))).isEqualTo(1);
        assertThat(Utils.getWeekNumber(LocalDate.of(2016, Month.SEPTEMBER, 1))).isEqualTo(1);

        assertThat(Utils.getWeekNumber(LocalDate.of(2015, Month.SEPTEMBER, 6))).isEqualTo(1);

        assertThat(Utils.getWeekNumber(LocalDate.of(2015, Month.SEPTEMBER, 7))).isEqualTo(2);
        assertThat(Utils.getWeekNumber(LocalDate.of(2015, Month.SEPTEMBER, 13))).isEqualTo(2);

        assertThat(Utils.getWeekNumber(LocalDate.of(2015, Month.SEPTEMBER, 14))).isEqualTo(3);
        assertThat(Utils.getWeekNumber(LocalDate.of(2015, Month.SEPTEMBER, 20))).isEqualTo(3);

        assertThat(Utils.getWeekNumber(LocalDate.of(2015, Month.SEPTEMBER, 21))).isEqualTo(4);
        assertThat(Utils.getWeekNumber(LocalDate.of(2015, Month.SEPTEMBER, 27))).isEqualTo(4);

        assertThat(Utils.getWeekNumber(LocalDate.of(2015, Month.DECEMBER, 31))).isEqualTo(2);
        assertThat(Utils.getWeekNumber(LocalDate.of(2016, Month.JANUARY, 1))).isEqualTo(2);

        assertThat(Utils.getWeekNumber(LocalDate.of(2015, Month.DECEMBER, 31))).isEqualTo(2);
        assertThat(Utils.getWeekNumber(LocalDate.of(2016, Month.JANUARY, 1))).isEqualTo(2);
        assertThat(Utils.getWeekNumber(LocalDate.of(2016, Month.JANUARY, 3))).isEqualTo(2);

        assertThat(Utils.getWeekNumber(LocalDate.of(2016, Month.JANUARY, 4))).isEqualTo(3);
    }
}