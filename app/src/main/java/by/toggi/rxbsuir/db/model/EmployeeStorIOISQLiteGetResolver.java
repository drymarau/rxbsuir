package by.toggi.rxbsuir.db.model;

import android.database.Cursor;
import android.support.annotation.NonNull;
import by.toggi.rxbsuir.EmployeeModel;
import by.toggi.rxbsuir.rest.model.Employee;
import com.google.gson.Gson;
import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver;
import java.util.ArrayList;
import java.util.Arrays;

public class EmployeeStorIOISQLiteGetResolver extends DefaultGetResolver<Employee> {

  private final Gson mGson;

  public EmployeeStorIOISQLiteGetResolver(Gson gson) {
    mGson = gson;
  }

  @NonNull @Override public Employee mapFromCursor(@NonNull Cursor cursor) {
    String[] academicDepartmentArray = mGson.fromJson(
        cursor.getString(cursor.getColumnIndex(EmployeeModel.ACADEMIC_DEPARTMENT_LIST)),
        String[].class);
    if (academicDepartmentArray == null) {
      academicDepartmentArray = new String[] {};
    }

    return Employee.newInstance(cursor.getInt(cursor.getColumnIndex(EmployeeModel.ID)),
        new ArrayList<>(Arrays.asList(academicDepartmentArray)),
        cursor.getString(cursor.getColumnIndex(EmployeeModel.FIRST_NAME)),
        cursor.getString(cursor.getColumnIndex(EmployeeModel.MIDDLE_NAME)),
        cursor.getString(cursor.getColumnIndex(EmployeeModel.LAST_NAME)),
        cursor.getInt(cursor.getColumnIndex(EmployeeModel.IS_CACHED)) != 0);
  }
}
