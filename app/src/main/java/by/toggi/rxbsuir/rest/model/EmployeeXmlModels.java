package by.toggi.rxbsuir.rest.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "employeeXmlModels", strict = false)
public class EmployeeXmlModels {

    @ElementList(entry = "employee", inline = true)
    public List<Employee> employeeList;

}
