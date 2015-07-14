package by.toggi.rxbsuir.rest.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(strict = false)
public class Schedule {

    @ElementList(entry = "auditory", inline = true, required = false)
    public List<String> auditory;
    @ElementList(entry = "employee", inline = true, required = false)
    public List<Employee> employeeList;
    @Element(name = "lessonTime")
    public String lessonTime;
    @Element(name = "lessonType", required = false)
    public String lessonType;
    @Element(name = "note", required = false)
    public String note;
    @Element(name = "numSubgroup", required = false)
    public int numSubgroup;
    @ElementList(entry = "studentGroup", inline = true, required = false)
    public List<Integer> studentGroupList;
    @Element(name = "subject", required = false)
    public String subject;
    @ElementList(entry = "weekNumber", inline = true, required = false)
    public List<Integer> weekNumberList;
    @Element(name = "zaoch", required = false)
    public boolean zaoch;

}
