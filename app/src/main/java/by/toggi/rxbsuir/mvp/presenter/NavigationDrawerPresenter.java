package by.toggi.rxbsuir.mvp.presenter;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.List;

import javax.inject.Inject;

import by.toggi.rxbsuir.mvp.Presenter;
import by.toggi.rxbsuir.mvp.view.NavigationDrawerView;
import by.toggi.rxbsuir.rest.model.Employee;
import by.toggi.rxbsuir.rest.model.StudentGroup;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

import static by.toggi.rxbsuir.db.RxBsuirContract.EmployeeEntry;
import static by.toggi.rxbsuir.db.RxBsuirContract.StudentGroupEntry;

public class NavigationDrawerPresenter implements Presenter<NavigationDrawerView> {

    private final StorIOSQLite mStorIOSQLite;
    private final Observable<List<StudentGroup>> mStudentGroupObservable;
    private final Observable<List<Employee>> mEmployeeListObservable;
    private NavigationDrawerView mNavigationDrawerView;
    private CompositeSubscription mCompositeSubscription;

    @Inject
    public NavigationDrawerPresenter(StorIOSQLite storIOSQLite) {
        mStorIOSQLite = storIOSQLite;
        mStudentGroupObservable = getGroupObservable();
        mEmployeeListObservable = getEmployeeObservable();
    }

    @Override
    public void onCreate() {
        // get all groups with is_cached = true as set
        // get all employees with is_cached = true as set
        // send them to View!
        mCompositeSubscription = new CompositeSubscription(
                mEmployeeListObservable.subscribe(this::onEmployeeSuccess),
                mStudentGroupObservable.subscribe(this::onGroupSuccess)
        );
    }

    private void onEmployeeSuccess(List<Employee> employeeList) {
        Observable.from(employeeList)
                .toMap(employee -> Long.valueOf(employee.id).intValue(), Employee::toString)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(map -> {
                    if (isViewAttached()) {
                        mNavigationDrawerView.updateEmployeeList(map);
                    }
                });
    }

    private void onGroupSuccess(List<StudentGroup> studentGroupList) {
        Observable.from(studentGroupList)
                .toMap(studentGroup -> Integer.valueOf(studentGroup.name), studentGroup -> studentGroup.name)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(map -> {
                    if (isViewAttached()) {
                        mNavigationDrawerView.updateGroupList(map);
                    }
                });
    }

    @Override
    public void onDestroy() {
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions() && !mCompositeSubscription.isUnsubscribed()) {
            mCompositeSubscription.unsubscribe();
        }
        detachView();
    }

    @Override
    public void attachView(NavigationDrawerView navigationDrawerView) {
        if (navigationDrawerView == null) {
            throw new NullPointerException("ScheduleView should not be null");
        }
        mNavigationDrawerView = navigationDrawerView;
    }

    @Override
    public void detachView() {
        mNavigationDrawerView = null;
    }

    @Override
    public String getTag() {
        return this.getClass().getSimpleName();
    }

    private Observable<List<StudentGroup>> getGroupObservable() {
        return mStorIOSQLite.get()
                .listOfObjects(StudentGroup.class)
                .withQuery(Query.builder()
                        .table(StudentGroupEntry.TABLE_NAME)
                        .where(StudentGroupEntry.COL_IS_CACHED + " = ?")
                        .whereArgs("1")
                        .build())
                .prepare()
                .createObservable();
    }

    private Observable<List<Employee>> getEmployeeObservable() {
        return mStorIOSQLite.get()
                .listOfObjects(Employee.class)
                .withQuery(Query.builder()
                        .table(EmployeeEntry.TABLE_NAME)
                        .where(EmployeeEntry.COL_IS_CACHED + " = ?")
                        .whereArgs("1")
                        .build())
                .prepare()
                .createObservable();
    }

    private boolean isViewAttached() {
        return mNavigationDrawerView != null;
    }
}
