package by.toggi.rxbsuir.db.model;

import android.support.annotation.NonNull;
import by.toggi.rxbsuir.EmployeeModel;
import by.toggi.rxbsuir.rest.model.Employee;
import com.pushtorefresh.storio.sqlite.operations.delete.DefaultDeleteResolver;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;

public class EmployeeStorIOSQLiteDeleteResolver extends DefaultDeleteResolver<Employee> {

  @NonNull @Override protected DeleteQuery mapToDeleteQuery(@NonNull Employee object) {
    return DeleteQuery.builder()
        .table(EmployeeModel.TABLE_NAME)
        .where(EmployeeModel.ID + " = ?")
        .whereArgs(object.id)
        .build();
  }
}
