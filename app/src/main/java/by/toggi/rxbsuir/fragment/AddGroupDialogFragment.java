package by.toggi.rxbsuir.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.Utils;
import by.toggi.rxbsuir.activity.ScheduleActivity;
import by.toggi.rxbsuir.mvp.presenter.AddGroupDialogPresenter;
import by.toggi.rxbsuir.mvp.presenter.SchedulePresenter;
import by.toggi.rxbsuir.mvp.view.AddGroupDialogView;
import by.toggi.rxbsuir.rest.model.StudentGroup;
import rx.Subscription;

public class AddGroupDialogFragment extends DialogFragment implements AddGroupDialogView {

    @Inject AddGroupDialogPresenter mPresenter;

    private ArrayAdapter<StudentGroup> mAdapter;
    private OnButtonClickListener mListener;
    private int mPosition = -1;
    private TextInputLayout mTextInputLayout;
    private Subscription mSubscription;

    public static AddGroupDialogFragment newInstance() {
        return new AddGroupDialogFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (OnButtonClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnButtonClickListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initializeComponent();

        FragmentManager manager = getFragmentManager();
        StorageFragment fragment = (StorageFragment) manager.findFragmentByTag(ScheduleActivity.TAG_STORAGE_FRAGMENT);

        if (fragment == null) {
            throw new IllegalStateException("Storage fragment should already be added");
        }
        if (fragment.getPresenter(mPresenter.getTag()) == null) {
            fragment.setPresenter(mPresenter.getTag(), mPresenter);
        } else {
            try {
                mPresenter = (AddGroupDialogPresenter) fragment.getPresenter(mPresenter.getTag());
            } catch (ClassCastException e) {
                throw new ClassCastException("Presenter must be of class AddDialogPresenter");
            }
        }

        mPresenter.attachView(this);
        mPresenter.onCreate();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mTextInputLayout = (TextInputLayout) View.inflate(getActivity(), R.layout.dialog_add_group, null);
        AutoCompleteTextView textView = ButterKnife.findById(mTextInputLayout, R.id.group_number_text_view);
        mAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, new ArrayList<>());
        textView.setAdapter(mAdapter);
        textView.setOnItemClickListener((parent, view, position, id) -> mPosition = position);
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity()).customView(mTextInputLayout, true)
                .title(R.string.title_add_group)
                .positiveText(R.string.positive_add)
                .negativeText(android.R.string.cancel)
                .autoDismiss(false)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        if (mPosition != -1) {
                            StudentGroup studentGroup = mAdapter.getItem(mPosition);
                            mListener.onPositiveButtonClicked(studentGroup.id, studentGroup.name, true);
                            mPosition = -1;
                            dismiss();
                        } else {
                            mTextInputLayout.setError(getString(R.string.error_list_group));
                        }
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        dismiss();
                    }
                })
                .build();
        // Input validation
        mSubscription = RxTextView.textChanges(textView)
                .map(charSequence -> mPresenter.isValidGroupNumber(charSequence.toString()))
                .startWith(false)
                .distinctUntilChanged()
                .subscribe(aBoolean -> dialog.getActionButton(DialogAction.POSITIVE).setEnabled(aBoolean));
        return dialog;
    }

    private void initializeComponent() {
        ((RxBsuirApplication) getActivity().getApplication()).getAppComponent().inject(this);
    }

    @Override
    public void updateStudentGroupList(List<StudentGroup> studentGroupList) {
        mTextInputLayout.setErrorEnabled(false);
        mAdapter.clear();
        for (StudentGroup group : studentGroupList) {
            mAdapter.add(group);
        }
    }

    @Override
    public void showError(SchedulePresenter.Error error) {
        if (mTextInputLayout != null) {
            mTextInputLayout.setErrorEnabled(true);
            switch (error) {
                case NETWORK:
                    mTextInputLayout.setError(getString(R.string.error_network));
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
        Utils.unsubscribe(mSubscription);
    }
}
