package by.toggi.rxbsuir.rest.model;

import androidx.annotation.NonNull;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class Employee {

    public int id;
    public List<String> academicDepartment;
    public String firstName;
    public String middleName;
    public String lastName;
    public boolean isCached;

    Employee() {
    }

    public static Employee newInstance(int id, List<String> academicDepartmentList, String firstName, String middleName, String lastName, boolean isCached) {
        var employee = new Employee();
        employee.id = id;
        employee.academicDepartment = academicDepartmentList;
        employee.firstName = firstName;
        employee.middleName = middleName;
        employee.lastName = lastName;
        employee.isCached = isCached;
        return employee;
    }

    @Override @NonNull public String toString() {
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
