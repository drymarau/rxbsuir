package by.toggi.rxbsuir.db.model;

import static by.toggi.rxbsuir.db.RxBsuirContract.EmployeeEntry;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.pushtorefresh.storio.sqlite.operations.put.DefaultPutResolver;
import com.pushtorefresh.storio.sqlite.queries.InsertQuery;
import com.pushtorefresh.storio.sqlite.queries.UpdateQuery;

import by.toggi.rxbsuir.rest.model.Employee;

public class EmployeeStorIOSQLitePutResolver extends DefaultPutResolver<Employee> {

  private final Gson mGson;

  public EmployeeStorIOSQLitePutResolver(Gson gson) {
    mGson = gson;
  }

  @NonNull @Override protected InsertQuery mapToInsertQuery(@NonNull Employee object) {
    return InsertQuery.builder().table(EmployeeEntry.TABLE_NAME).build();
  }

  @NonNull @Override protected UpdateQuery mapToUpdateQuery(@NonNull Employee object) {
    return UpdateQuery.builder()
        .table(EmployeeEntry.TABLE_NAME)
        .where(EmployeeEntry.COL_ID + " = ?")
        .whereArgs(object.id)
        .build();
  }

  @NonNull @Override protected ContentValues mapToContentValues(@NonNull Employee object) {
    var contentValues = new ContentValues(5);

    contentValues.put(EmployeeEntry.COL_ID, object.id);
    contentValues.put(EmployeeEntry.COL_ACADEMIC_DEPARTMENT_LIST,
        mGson.toJson(object.academicDepartment));
    contentValues.put(EmployeeEntry.COL_FIRST_NAME, object.firstName);
    contentValues.put(EmployeeEntry.COL_MIDDLE_NAME, object.middleName);
    contentValues.put(EmployeeEntry.COL_LAST_NAME, object.lastName);
    contentValues.put(EmployeeEntry.COL_IS_CACHED, object.isCached);

    return contentValues;
  }
}
