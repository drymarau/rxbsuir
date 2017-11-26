package by.toggi.rxbsuir.rest.model;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.List;
import paperparcel.PaperParcel;

@PaperParcel public class Employee implements Parcelable {

  public static final Creator<Employee> CREATOR = PaperParcelEmployee.CREATOR;

  public int id;
  public List<String> academicDepartment;
  public String firstName;
  public String middleName;
  public String lastName;
  public boolean isCached;

  Employee() {
  }

  public static Employee newInstance(int id, List<String> academicDepartmentList, String firstName,
      String middleName, String lastName, boolean isCached) {
    Employee employee = new Employee();
    employee.id = id;
    employee.academicDepartment = academicDepartmentList;
    employee.firstName = firstName;
    employee.middleName = middleName;
    employee.lastName = lastName;
    employee.isCached = isCached;
    return employee;
  }

  @Override public String toString() {
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

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    PaperParcelEmployee.writeToParcel(this, dest, flags);
  }
}
