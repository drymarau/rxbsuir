package by.toggi.rxbsuir.mvp.presenter;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import by.toggi.rxbsuir.Utils;
import by.toggi.rxbsuir.mvp.Presenter;
import by.toggi.rxbsuir.mvp.view.NavigationDrawerView;
import by.toggi.rxbsuir.rest.model.Employee;
import by.toggi.rxbsuir.rest.model.StudentGroup;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

@Singleton
public class NavigationDrawerPresenter extends Presenter<NavigationDrawerView> {

    private final Observable<List<StudentGroup>> mStudentGroupObservable;
    private final Observable<List<Employee>> mEmployeeListObservable;
    private CompositeSubscription mCompositeSubscription;

    @Inject
    public NavigationDrawerPresenter() {
        mStudentGroupObservable = getGroupObservable();
        mEmployeeListObservable = getEmployeeObservable();
    }

    @Override
    public void onCreate() {
        mCompositeSubscription = new CompositeSubscription(
                mEmployeeListObservable.subscribe(this::onEmployeeSuccess),
                mStudentGroupObservable.subscribe(this::onGroupSuccess)
        );
    }

    @Override
    public void onDestroy() {
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
        return Observable.never();
    }

    private Observable<List<Employee>> getEmployeeObservable() {
        return Observable.never();
    }
}
