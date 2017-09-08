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

@RunWith(AndroidJUnit4.class) public class UtilsTest extends ApplicationTestCase<Application> {

  private final Application application;

  public UtilsTest() {
    super(Application.class);
    application = (Application) InstrumentationRegistry.getTargetContext().getApplicationContext();
  }

  @Before public void setup() {
    AndroidThreeTen.init(application);
  }

  @Test public void litmus() {
    assertThat(ZoneRulesProvider.getAvailableZoneIds()).isNotEmpty();
  }

  @Test public void getWeekNumberTest() {
    assertThat(Utils.getWeekNumber(LocalDate.of(2017, Month.SEPTEMBER, 1))).isEqualTo(1);
    assertThat(Utils.getWeekNumber(LocalDate.of(2018, Month.SEPTEMBER, 1))).isEqualTo(1);

    assertThat(Utils.getWeekNumber(LocalDate.of(2017, Month.SEPTEMBER, 4))).isEqualTo(2);

    assertThat(Utils.getWeekNumber(LocalDate.of(2017, Month.SEPTEMBER, 8))).isEqualTo(2);
    assertThat(Utils.getWeekNumber(LocalDate.of(2017, Month.SEPTEMBER, 11))).isEqualTo(3);

    assertThat(Utils.getWeekNumber(LocalDate.of(2017, Month.SEPTEMBER, 18))).isEqualTo(4);
    assertThat(Utils.getWeekNumber(LocalDate.of(2017, Month.SEPTEMBER, 22))).isEqualTo(4);
  }
}
