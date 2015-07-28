package by.toggi.rxbsuir.db.model;

import android.support.annotation.NonNull;

import com.pushtorefresh.storio.sqlite.operations.delete.DefaultDeleteResolver;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;

import by.toggi.rxbsuir.rest.model.Employee;

import static by.toggi.rxbsuir.db.RxBsuirContract.EmployeeEntry;

public class EmployeeStorIOSQLiteDeleteResolver extends DefaultDeleteResolver<Employee> {

    @NonNull
    @Override
    protected DeleteQuery mapToDeleteQuery(Employee object) {
        return DeleteQuery.builder()
                .table(EmployeeEntry.TABLE_NAME)
                .where(EmployeeEntry.COL_ID + " = ?")
                .whereArgs(object.id)
                .build();
    }

}
