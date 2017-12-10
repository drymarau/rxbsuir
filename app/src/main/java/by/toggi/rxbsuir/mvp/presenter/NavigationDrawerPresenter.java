package by.toggi.rxbsuir.mvp.presenter;

import by.toggi.rxbsuir.EmployeeModel;
import by.toggi.rxbsuir.GroupModel;
import by.toggi.rxbsuir.Utils;
import by.toggi.rxbsuir.mvp.Presenter;
import by.toggi.rxbsuir.mvp.view.NavigationDrawerView;
import by.toggi.rxbsuir.rest.model.Employee;
import by.toggi.rxbsuir.rest.model.StudentGroup;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;
import java.util.List;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class NavigationDrawerPresenter extends Presenter<NavigationDrawerView> {

  private final StorIOSQLite mStorIOSQLite;
  private final Observable<List<StudentGroup>> mStudentGroupObservable;
  private final Observable<List<Employee>> mEmployeeListObservable;
  private CompositeSubscription mCompositeSubscription;

  public NavigationDrawerPresenter(StorIOSQLite storIOSQLite) {
    mStorIOSQLite = storIOSQLite;
    mStudentGroupObservable = getGroupObservable();
    mEmployeeListObservable = getEmployeeObservable();
  }

  @Override public void onCreate() {
    mCompositeSubscription =
        new CompositeSubscription(mEmployeeListObservable.subscribe(this::onEmployeeSuccess),
            mStudentGroupObservable.subscribe(this::onGroupSuccess));
  }

  @Override public void onDestroy() {
    Utils.unsubscribeComposite(mCompositeSubscription);
    detachView();
  }

  private void onEmployeeSuccess(List<Employee> employeeList) {
    Observable.from(employeeList)
        .toMap(employee -> employee.id, Employee::toString)
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(map -> {
          if (isViewAttached()) {
            getView().updateEmployeeList(map);
          }
        });
  }

  private void onGroupSuccess(List<StudentGroup> studentGroupList) {
    Observable.from(studentGroupList)
        .toMap(studentGroup -> studentGroup.id, studentGroup -> studentGroup.name)
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(map -> {
          if (isViewAttached()) {
            getView().updateGroupList(map);
          }
        });
  }

  private Observable<List<StudentGroup>> getGroupObservable() {
    return mStorIOSQLite.get()
        .listOfObjects(StudentGroup.class)
        .withQuery(Query.builder()
            .table(GroupModel.TABLE_NAME)
            .where(GroupModel.IS_CACHED + " = ?")
            .whereArgs("1")
            .build())
        .prepare()
        .createObservable();
  }

  private Observable<List<Employee>> getEmployeeObservable() {
    return mStorIOSQLite.get()
        .listOfObjects(Employee.class)
        .withQuery(Query.builder()
            .table(EmployeeModel.TABLE_NAME)
            .where(EmployeeModel.IS_CACHED + " = ?")
            .whereArgs("1")
            .build())
        .prepare()
        .createObservable();
  }
}
