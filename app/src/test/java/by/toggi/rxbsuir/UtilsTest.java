package by.toggi.rxbsuir;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;

import rx.Subscription;

public class UtilsTest {

    @Ignore @Test public void getWeekNumberTest() {
        assertThat(Utils.getWeekNumber(LocalDate.of(2017, Month.SEPTEMBER, 1))).isEqualTo(1);
        assertThat(Utils.getWeekNumber(LocalDate.of(2018, Month.SEPTEMBER, 1))).isEqualTo(1);

        assertThat(Utils.getWeekNumber(LocalDate.of(2017, Month.SEPTEMBER, 4))).isEqualTo(2);

        assertThat(Utils.getWeekNumber(LocalDate.of(2017, Month.SEPTEMBER, 8))).isEqualTo(2);
        assertThat(Utils.getWeekNumber(LocalDate.of(2017, Month.SEPTEMBER, 11))).isEqualTo(3);

        assertThat(Utils.getWeekNumber(LocalDate.of(2017, Month.SEPTEMBER, 18))).isEqualTo(4);
        assertThat(Utils.getWeekNumber(LocalDate.of(2017, Month.SEPTEMBER, 22))).isEqualTo(4);
    }

    @Test public void convertWeekdayToDayOfWeekTest() {
        assertThat(Utils.convertWeekdayToDayOfWeek("понедельник")).isEqualTo(DayOfWeek.MONDAY);
        assertThat(Utils.convertWeekdayToDayOfWeek("ВТОРНИК")).isEqualTo(DayOfWeek.TUESDAY);
        assertThat(Utils.convertWeekdayToDayOfWeek("СРедА")).isEqualTo(DayOfWeek.WEDNESDAY);
        assertThat(Utils.convertWeekdayToDayOfWeek("четверг")).isEqualTo(DayOfWeek.THURSDAY);
        assertThat(Utils.convertWeekdayToDayOfWeek("Пятница")).isEqualTo(DayOfWeek.FRIDAY);
        assertThat(Utils.convertWeekdayToDayOfWeek("суББота")).isEqualTo(DayOfWeek.SATURDAY);
    }

    @Test(expected = IllegalArgumentException.class) public void convertWeekdayToDayOfWeekTestIllegalArgumentTest() {
        Utils.convertWeekdayToDayOfWeek("dsfgdsfg");
    }

    @Test public void unsubscribeTest() {
        var subscription = Mockito.mock(Subscription.class);
        Utils.unsubscribe(subscription);
        verify(subscription, times(1)).unsubscribe();
        verify(subscription, times(1)).isUnsubscribed();
    }

}
