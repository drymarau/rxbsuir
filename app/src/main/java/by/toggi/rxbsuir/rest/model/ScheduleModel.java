package by.toggi.rxbsuir.rest.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(strict = false)
public class ScheduleModel {

    @Element(name = "weekDay")
    public String weekDay;
    @ElementList(entry = "schedule", inline = true)
    public List<Schedule> scheduleList;

}
