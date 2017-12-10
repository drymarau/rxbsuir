package by.toggi.rxbsuir.db.model;

import android.content.ContentValues;
import android.support.annotation.NonNull;
import by.toggi.rxbsuir.EmployeeModel;
import by.toggi.rxbsuir.rest.model.Employee;
import com.google.gson.Gson;
import com.pushtorefresh.storio.sqlite.operations.put.DefaultPutResolver;
import com.pushtorefresh.storio.sqlite.queries.InsertQuery;
import com.pushtorefresh.storio.sqlite.queries.UpdateQuery;

public class EmployeeStorIOSQLitePutResolver extends DefaultPutResolver<Employee> {

  private final Gson mGson;

  public EmployeeStorIOSQLitePutResolver(Gson gson) {
    mGson = gson;
  }

  @NonNull @Override protected InsertQuery mapToInsertQuery(@NonNull Employee object) {
    return InsertQuery.builder().table(EmployeeModel.TABLE_NAME).build();
  }

  @NonNull @Override protected UpdateQuery mapToUpdateQuery(@NonNull Employee object) {
    return UpdateQuery.builder()
        .table(EmployeeModel.TABLE_NAME)
        .where(EmployeeModel.ID + " = ?")
        .whereArgs(object.id)
        .build();
  }

  @NonNull @Override protected ContentValues mapToContentValues(@NonNull Employee object) {
    ContentValues contentValues = new ContentValues(5);

    contentValues.put(EmployeeModel.ID, object.id);
    contentValues.put(EmployeeModel.ACADEMIC_DEPARTMENT_LIST,
        mGson.toJson(object.academicDepartment));
    contentValues.put(EmployeeModel.FIRST_NAME, object.firstName);
    contentValues.put(EmployeeModel.MIDDLE_NAME, object.middleName);
    contentValues.put(EmployeeModel.LAST_NAME, object.lastName);
    contentValues.put(EmployeeModel.IS_CACHED, object.isCached);

    return contentValues;
  }
}
