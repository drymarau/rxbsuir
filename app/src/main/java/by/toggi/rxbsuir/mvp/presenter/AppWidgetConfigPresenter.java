package by.toggi.rxbsuir.mvp.presenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import by.toggi.rxbsuir.SyncIdItem;
import by.toggi.rxbsuir.Utils;
import by.toggi.rxbsuir.mvp.Presenter;
import by.toggi.rxbsuir.mvp.view.AppWidgetConfigView;
import rx.Observable;
import rx.Subscription;

public class AppWidgetConfigPresenter extends Presenter<AppWidgetConfigView> {

    private Subscription mSubscription;

    @Inject
    public AppWidgetConfigPresenter() {
    }

    @Override
    public void onCreate() {
        mSubscription = Observable.combineLatest(
                getGroupObservable(),
                getEmployeeObservable(),
                (groupList, employeeList) -> {
                    var syncIdItemList = new ArrayList<>(groupList);
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
        return Observable.never();
    }

    private Observable<List<SyncIdItem>> getEmployeeObservable() {
        return Observable.never();
    }
}
