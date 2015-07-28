package by.toggi.rxbsuir.mvp.presenter;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.put.PutResults;
import com.pushtorefresh.storio.sqlite.queries.Query;

import java.util.List;

import javax.inject.Inject;

import by.toggi.rxbsuir.db.RxBsuirContract;
import by.toggi.rxbsuir.mvp.Presenter;
import by.toggi.rxbsuir.mvp.view.AddGroupDialogView;
import by.toggi.rxbsuir.rest.BsuirService;
import by.toggi.rxbsuir.rest.model.StudentGroup;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

public class AddGroupDialogPresenter implements Presenter<AddGroupDialogView> {

    private Observable<List<StudentGroup>> mGroupListObservable;
    private List<String> mGroupNumberList;
    private AddGroupDialogView mAddGroupDialogView;
    private BsuirService mService;
    private StorIOSQLite mStorIOSQLite;
    private Subscription mSubscription;

    @Inject
    public AddGroupDialogPresenter(BsuirService service, StorIOSQLite storIOSQLite) {
        mService = service;
        mStorIOSQLite = storIOSQLite;
        mGroupListObservable = storIOSQLite.get()
                .listOfObjects(StudentGroup.class)
                .withQuery(Query.builder()
                        .table(RxBsuirContract.StudentGroupEntry.TABLE_NAME)
                        .build())
                .prepare()
                .createObservable()
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public void onCreate() {
        mSubscription = mGroupListObservable.subscribe(studentGroups -> {
            if (studentGroups == null || studentGroups.isEmpty()) {
                getStudentGroupsFromNetwork();
            } else {
                updateStudentGroupListInView(studentGroups);
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
    public void attachView(AddGroupDialogView addGroupDialogView) {
        if (addGroupDialogView == null) {
            throw new NullPointerException("AddGroupDialogView should not be null");
        }
        mAddGroupDialogView = addGroupDialogView;
    }

    @Override
    public void detachView() {
        mAddGroupDialogView = null;
    }

    /**
     * Validates group number.
     *
     * @param groupNumber the group number
     * @return true is group number is valid, false otherwise
     */
    public boolean isValidGroupNumber(String groupNumber) {
        return mGroupNumberList != null && mGroupNumberList.contains(groupNumber);
    }

    private void updateStudentGroupListInView(List<StudentGroup> studentGroupList) {
        Observable.from(studentGroupList)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .map(StudentGroup::toString)
                .toList()
                .subscribe(strings -> {
                    mGroupNumberList = strings;
                    if (isViewAttached()) {
                        mAddGroupDialogView.updateStudentGroupList(mGroupNumberList);
                    }
                });
    }

    private void getStudentGroupsFromNetwork() {
        mService.getStudentGroups()
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .map(studentGroupXmlModels -> studentGroupXmlModels.studentGroupList)
                .flatMap(this::getStudentGroupPutObservable)
                .subscribe(studentGroupPutResults -> Timber.d("Insert count: %d", studentGroupPutResults.numberOfInserts()));
    }

    private Observable<PutResults<StudentGroup>> getStudentGroupPutObservable(List<StudentGroup> studentGroupList) {
        return mStorIOSQLite.put()
                .objects(studentGroupList)
                .prepare()
                .createObservable();
    }

    private boolean isViewAttached() {
        return mAddGroupDialogView != null;
    }
}
