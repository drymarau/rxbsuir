package by.toggi.rxbsuir.mvp.presenter;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import java.util.List;

import javax.inject.Inject;

import by.toggi.rxbsuir.mvp.Presenter;
import by.toggi.rxbsuir.mvp.view.AddDialogView;
import by.toggi.rxbsuir.rest.BsuirService;

public class AddDialogPresenter implements Presenter<AddDialogView> {

    private List<String> mGroupNumberList;
    private AddDialogView mAddDialogView;
    private BsuirService mService;
    private StorIOSQLite mStorIOSQLite;

    @Inject
    public AddDialogPresenter(BsuirService service, StorIOSQLite storIOSQLite) {
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
    public void attachView(AddDialogView addDialogView) {
        mAddDialogView = addDialogView;
    }

    @Override
    public void detachView() {
        mAddDialogView = null;
    }

    /**
     * Validates group number.
     *
     * @param groupNumber the group number
     * @return true is group number is valid, false otherwise
     */
    public boolean isValidGroupNumber(String groupNumber) {
        return mGroupNumberList == null || mGroupNumberList.contains(groupNumber);
    }
}
