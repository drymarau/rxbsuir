package by.toggi.rxbsuir.rest.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "scheduleXmlModels", strict = false)
public class ScheduleXmlModels {

    @ElementList(entry = "scheduleModel", inline = true)
    public List<ScheduleModel> scheduleModelList;

}
