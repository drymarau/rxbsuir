package by.toggi.rxbsuir.mvp;

import java.util.List;

import by.toggi.rxbsuir.model.Schedule;

public interface ScheduleView extends View {

    void showScheduleList(List<Schedule> scheduleList);

}
