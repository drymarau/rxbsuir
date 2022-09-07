package by.toggi.rxbsuir.db.model;

import static by.toggi.rxbsuir.db.RxBsuirContract.EmployeeEntry;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver;

import java.util.ArrayList;
import java.util.Arrays;

import by.toggi.rxbsuir.rest.model.Employee;

public class EmployeeStorIOISQLiteGetResolver extends DefaultGetResolver<Employee> {

  private final Gson mGson;

  public EmployeeStorIOISQLiteGetResolver(Gson gson) {
    mGson = gson;
  }

  @NonNull @Override public Employee mapFromCursor(@NonNull Cursor cursor) {
    var academicDepartmentArray = mGson.fromJson(
        cursor.getString(cursor.getColumnIndex(EmployeeEntry.COL_ACADEMIC_DEPARTMENT_LIST)),
        String[].class);
    if (academicDepartmentArray == null) {
      academicDepartmentArray = new String[] {};
    }

    return Employee.newInstance(cursor.getInt(cursor.getColumnIndex(EmployeeEntry.COL_ID)),
        new ArrayList<>(Arrays.asList(academicDepartmentArray)),
        cursor.getString(cursor.getColumnIndex(EmployeeEntry.COL_FIRST_NAME)),
        cursor.getString(cursor.getColumnIndex(EmployeeEntry.COL_MIDDLE_NAME)),
        cursor.getString(cursor.getColumnIndex(EmployeeEntry.COL_LAST_NAME)),
        cursor.getInt(cursor.getColumnIndex(EmployeeEntry.COL_IS_CACHED)) != 0);
  }
}
