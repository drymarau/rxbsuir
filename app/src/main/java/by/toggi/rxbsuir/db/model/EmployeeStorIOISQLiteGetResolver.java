package by.toggi.rxbsuir.db.model;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.pushtorefresh.storio.sqlite.operations.get.DefaultGetResolver;

import java.util.ArrayList;
import java.util.Arrays;

import by.toggi.rxbsuir.rest.model.Employee;

import static by.toggi.rxbsuir.db.RxBsuirContract.EmployeeEntry;

public class EmployeeStorIOISQLiteGetResolver extends DefaultGetResolver<Employee> {

    @NonNull
    @Override
    public Employee mapFromCursor(@NonNull Cursor cursor) {
        Gson gson = new Gson();
        String[] academicDepartmentArray = gson.fromJson(
                cursor.getString(cursor.getColumnIndex(EmployeeEntry.COL_ACADEMIC_DEPARTMENT_LIST)),
                String[].class
        );
        if (academicDepartmentArray == null) {
            academicDepartmentArray = new String[]{};
        }

        return Employee.newInstance(
                cursor.getLong(cursor.getColumnIndex(EmployeeEntry.COL_ID)),
                new ArrayList<>(Arrays.asList(academicDepartmentArray)),
                cursor.getString(cursor.getColumnIndex(EmployeeEntry.COL_FIRST_NAME)),
                cursor.getString(cursor.getColumnIndex(EmployeeEntry.COL_MIDDLE_NAME)),
                cursor.getString(cursor.getColumnIndex(EmployeeEntry.COL_LAST_NAME))
        );
    }
}
