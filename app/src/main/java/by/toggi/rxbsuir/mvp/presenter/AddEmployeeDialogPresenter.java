package by.toggi.rxbsuir.mvp.presenter;

import java.util.List;

import javax.inject.Inject;

import by.toggi.rxbsuir.Utils;
import by.toggi.rxbsuir.mvp.Presenter;
import by.toggi.rxbsuir.mvp.view.AddEmployeeDialogView;
import by.toggi.rxbsuir.rest.BsuirService;
import by.toggi.rxbsuir.rest.model.Employee;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddEmployeeDialogPresenter extends Presenter<AddEmployeeDialogView> {

    private final BsuirService mService;
    private final Observable<List<Employee>> mEmployeeListObservable;
    private Subscription mSubscription;
    private List<String> mEmployeeStringList;

    @Inject
    public AddEmployeeDialogPresenter(BsuirService service) {
        mService = service;
        mEmployeeListObservable = Observable.never();
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
        Utils.unsubscribe(mSubscription);
        detachView();
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
        Observable.from(employeeList).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .map(Employee::toString)
                .toList()
                .subscribe(strings -> {
                    mEmployeeStringList = strings;
                    if (isViewAttached()) {
                        getView().updateEmployeeList(employeeList);
                    }
                });
    }

    private void getEmployeeListFromNetwork() {
        mService.getEmployees()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(employeePutResults -> {}, this::onError);
    }

    private void onError(Throwable throwable) {
        if (throwable.getCause().toString().contains("java.net.UnknownHostException")) {
            if (isViewAttached()) {
                getView().showError(SchedulePresenter.Error.NETWORK);
            }
        }
    }
}
