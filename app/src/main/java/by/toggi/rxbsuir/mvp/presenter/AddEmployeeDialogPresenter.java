package by.toggi.rxbsuir.mvp.presenter;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.List;

import javax.inject.Inject;

import by.toggi.rxbsuir.db.RxBsuirContract;
import by.toggi.rxbsuir.mvp.Presenter;
import by.toggi.rxbsuir.mvp.view.AddEmployeeDialogView;
import by.toggi.rxbsuir.rest.BsuirService;
import by.toggi.rxbsuir.rest.model.Employee;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class AddEmployeeDialogPresenter implements Presenter<AddEmployeeDialogView> {

    private final BsuirService mService;
    private final StorIOSQLite mStorIOSQLite;
    private final Observable<List<Employee>> mEmployeeListObservable;
    private AddEmployeeDialogView mAddEmployeeDialogView;
    private Subscription mSubscription;
    private List<String> mEmployeeStringList;

    @Inject
    public AddEmployeeDialogPresenter(BsuirService service, StorIOSQLite storIOSQLite) {
        mService = service;
        mStorIOSQLite = storIOSQLite;
        mEmployeeListObservable = mStorIOSQLite.get()
                .listOfObjects(Employee.class)
                .withQuery(Query.builder()
                        .table(RxBsuirContract.EmployeeEntry.TABLE_NAME)
                        .build())
                .prepare()
                .createObservable()
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void onCreate() {
        mSubscription = mEmployeeListObservable.subscribe(employeeList -> {
            if (employeeList == null || employeeList.isEmpty()) {
                getEmployeeListFromNetwork();
            } else {
                updateEmployeeListInView(employeeList);
            }
        });
    }

    @Override
    public void onDestroy() {
        if (!mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        detachView();
    }

    @Override
    public void attachView(AddEmployeeDialogView addEmployeeDialogView) {
        if (addEmployeeDialogView == null) {
            throw new NullPointerException("AddGroupDialogView should not be null");
        }
        mAddEmployeeDialogView = addEmployeeDialogView;
    }

    @Override
    public void detachView() {
        mAddEmployeeDialogView = null;
    }

    @Override
    public String getTag() {
        return this.getClass().getSimpleName();
    }

    /**
     * Validates employeeString.
     *
     * @param employeeString the group number
     * @return true is group number is valid, false otherwise
     */
    public boolean isValidEmployee(String employeeString) {
        return mEmployeeStringList != null && mEmployeeStringList.contains(employeeString);
    }

    private void updateEmployeeListInView(List<Employee> employeeList) {
        Observable.from(employeeList).subscribeOn(Schedulers.computation()).observeOn(Schedulers.computation())
                .map(Employee::toString)
                .toList()
                .subscribe(strings -> mEmployeeStringList = strings);
        if (isViewAttached()) {
            mAddEmployeeDialogView.updateEmployeeList(employeeList);
        }
    }

    private void getEmployeeListFromNetwork() {
        mService.getEmployees()
                .observeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(employeeXmlModels -> employeeXmlModels.employeeList)
                .flatMap(this::getEmployeePutObservable)
                .subscribe(employeePutResults -> Timber.d("Insert count: %d", employeePutResults.numberOfInserts()));
    }

    private Observable<PutResults<Employee>> getEmployeePutObservable(List<Employee> employeeList) {
        return mStorIOSQLite.put()
                .objects(employeeList)
                .prepare()
                .createObservable();
    }

    private boolean isViewAttached() {
        return mAddEmployeeDialogView != null;
    }
}
