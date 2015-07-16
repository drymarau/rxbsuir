package by.toggi.rxbsuir.rest.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "studentGroupXmlModels")
public class StudentGroupXmlModels {

    @ElementList(entry = "scheduleModel", inline = true)
    List<StudentGroup> studentGroupList;

}
