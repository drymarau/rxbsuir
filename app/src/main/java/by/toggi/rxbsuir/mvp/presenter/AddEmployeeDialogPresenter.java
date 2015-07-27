package by.toggi.rxbsuir.mvp.presenter;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import javax.inject.Inject;

import by.toggi.rxbsuir.mvp.Presenter;
import by.toggi.rxbsuir.mvp.view.AddEmployeeDialogView;
import by.toggi.rxbsuir.rest.BsuirService;

public class AddEmployeeDialogPresenter implements Presenter<AddEmployeeDialogView> {

    private final BsuirService mService;
    private final StorIOSQLite mStorIOSQLite;
    private AddEmployeeDialogView mAddEmployeeDialogView;

    @Inject
    public AddEmployeeDialogPresenter(BsuirService service, StorIOSQLite storIOSQLite) {
        mService = service;
        mStorIOSQLite = storIOSQLite;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDestroy() {
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

    private boolean isViewAttached() {
        return mAddEmployeeDialogView != null;
    }
}
