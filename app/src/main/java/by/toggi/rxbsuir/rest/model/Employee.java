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
    @Element(name = "middleName", required = false)
    public String middleName;
    @Element(name = "lastName")
    public String lastName;
    public boolean isCached;

    Employee() {
    }

    public static Employee newInstance(long id, List<String> academicDepartmentList, String firstName, String middleName, String lastName, boolean isCached) {
        Employee employee = new Employee();
        employee.id = id;
        employee.academicDepartment = academicDepartmentList;
        employee.firstName = firstName;
        employee.middleName = middleName;
        employee.lastName = lastName;
        employee.isCached = isCached;
        return employee;
    }

    @Override
    public String toString() {
        if (middleName == null) {
            return String.format("%s %s", lastName, firstName);
        } else {
            return String.format("%s %s %s", lastName, firstName, middleName);
        }
    }

    public String getShortFullName() {
        if (middleName == null) {
            return String.format("%s %s.", lastName, firstName.charAt(0));
        } else {
            return String.format("%s %s.%s.", lastName, firstName.charAt(0), middleName.charAt(0));
        }
    }

}
