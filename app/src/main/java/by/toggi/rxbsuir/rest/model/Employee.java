package by.toggi.rxbsuir.rest.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

import java.util.List;

public class Employee {

    @Element(name = "id")
    public long id;
    @ElementList(entry = "academicDepartment", inline = true, required = false)
    public List<String> academicDepartment;
    @Element(name = "firstName")
    public String firstName;
    @Element(name = "middleName")
    public String middleName;
    @Element(name = "lastName")
    public String lastName;

    Employee() {
    }

    @Override
    public String toString() {
        return String.format("%s %s.%s.", lastName, firstName.charAt(0), middleName.charAt(0));
    }
}
