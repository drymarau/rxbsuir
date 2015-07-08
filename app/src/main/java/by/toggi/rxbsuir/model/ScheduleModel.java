package by.toggi.rxbsuir.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.List;

public class ScheduleModel {

    @Element(name = "weekDay")
    public String weekDay;
    @ElementList(entry = "schedule", inline = true)
    public List<Schedule> scheduleList;

}
