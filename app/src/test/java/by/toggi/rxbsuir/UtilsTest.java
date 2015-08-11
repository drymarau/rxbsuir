package by.toggi.rxbsuir;

import org.junit.Test;
import org.mockito.Mockito;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;

import java.util.List;

import rx.Subscription;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class UtilsTest {

    @Test
    public void convertWeekdayToDayOfWeekTest() {
        assertThat(Utils.convertWeekdayToDayOfWeek("понедельник")).isEqualTo(DayOfWeek.MONDAY);
        assertThat(Utils.convertWeekdayToDayOfWeek("ВТОРНИК")).isEqualTo(DayOfWeek.TUESDAY);
        assertThat(Utils.convertWeekdayToDayOfWeek("СРедА")).isEqualTo(DayOfWeek.WEDNESDAY);
        assertThat(Utils.convertWeekdayToDayOfWeek("четверг")).isEqualTo(DayOfWeek.THURSDAY);
        assertThat(Utils.convertWeekdayToDayOfWeek("Пятница")).isEqualTo(DayOfWeek.FRIDAY);
        assertThat(Utils.convertWeekdayToDayOfWeek("суББота")).isEqualTo(DayOfWeek.SATURDAY);
    }

    @Test(expected = IllegalArgumentException.class)
    public void convertWeekdayToDayOfWeekTestIllegalArgumentTest() {
        Utils.convertWeekdayToDayOfWeek("dsfgdsfg");
    }

    @Test
    public void unsubscribeTest() {
        Subscription subscription = Mockito.mock(Subscription.class);
        Utils.unsubscribe(subscription);
        verify(subscription, times(1)).unsubscribe();
        verify(subscription, times(1)).isUnsubscribed();
    }

    @Test
    public void getDateListTest() {
        List<LocalDate> dateList = Utils.getDateList("2015-09-01", "2015-12-28");
        assertThat(dateList).hasSize(102);
        assertThat(dateList.get(dateList.size() - 1)).isEqualTo(LocalDate.of(2015, 12, 28));
    }

}
