package by.toggi.rxbsuir.mvp.presenter;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import by.toggi.rxbsuir.SyncIdItem;
import by.toggi.rxbsuir.Utils;
import by.toggi.rxbsuir.mvp.Presenter;
import by.toggi.rxbsuir.mvp.view.AppWidgetConfigView;
import by.toggi.rxbsuir.rest.model.Employee;
import by.toggi.rxbsuir.rest.model.StudentGroup;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static by.toggi.rxbsuir.db.RxBsuirContract.EmployeeEntry;
import static by.toggi.rxbsuir.db.RxBsuirContract.StudentGroupEntry;

public class AppWidgetConfigPresenter extends Presenter<AppWidgetConfigView> {

    private final StorIOSQLite mStorIOSQLite;
    private Subscription mSubscription;

    @Inject
    public AppWidgetConfigPresenter(StorIOSQLite storIOSQLite) {
        mStorIOSQLite = storIOSQLite;
    }

    @Override
    public void onCreate() {
        mSubscription = Observable.combineLatest(
                getGroupObservable(),
                getEmployeeObservable(),
                (groupList, employeeList) -> {
                    List<SyncIdItem> syncIdItemList = new ArrayList<>(groupList);
                    syncIdItemList.addAll(employeeList);
                    return syncIdItemList;
                }
        ).subscribe(map -> {
            if (isViewAttached()) {
                getView().updateSyncIdList(map);
            }
        });
    }

    @Override
    public void onDestroy() {
        Utils.unsubscribe(mSubscription);
        detachView();
    }

    private Observable<List<SyncIdItem>> getGroupObservable() {
        return mStorIOSQLite.get()
                .listOfObjects(StudentGroup.class)
                .withQuery(Query.builder()
                        .table(StudentGroupEntry.TABLE_NAME)
                        .where(StudentGroupEntry.COL_IS_CACHED + " = ?")
                        .whereArgs("1")
                        .build())
                .prepare()
                .createObservable()
                .take(1)
                .flatMap(Observable::from)
                .map(studentGroup -> new SyncIdItem(studentGroup.id, studentGroup.name, true))
                .toList()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Observable<List<SyncIdItem>> getEmployeeObservable() {
        return mStorIOSQLite.get()
                .listOfObjects(Employee.class)
                .withQuery(Query.builder()
                        .table(EmployeeEntry.TABLE_NAME)
                        .where(EmployeeEntry.COL_IS_CACHED + " = ?")
                        .whereArgs("1")
                        .build())
                .prepare()
                .createObservable()
                .take(1)
                .flatMap(Observable::from)
                .map(employee -> new SyncIdItem(employee.id, employee.getShortFullName(), false))
                .toList()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
