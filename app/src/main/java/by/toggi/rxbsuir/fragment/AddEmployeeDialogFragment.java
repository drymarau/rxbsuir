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
import by.toggi.rxbsuir.mvp.presenter.AddEmployeeDialogPresenter;
import by.toggi.rxbsuir.mvp.presenter.SchedulePresenter;
import by.toggi.rxbsuir.mvp.view.AddEmployeeDialogView;
import by.toggi.rxbsuir.rest.model.Employee;
import rx.Subscription;

public class AddEmployeeDialogFragment extends DialogFragment implements AddEmployeeDialogView {

    @Inject AddEmployeeDialogPresenter mPresenter;

    private ArrayAdapter<Employee> mAdapter;
    private OnButtonClickListener mListener;
    private int mPosition = -1;
    private TextInputLayout mTextInputLayout;
    private Subscription mSubscription;

    public static AddEmployeeDialogFragment newInstance() {
        return new AddEmployeeDialogFragment();
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
                mPresenter = (AddEmployeeDialogPresenter) fragment.getPresenter(mPresenter.getTag());
            } catch (ClassCastException e) {
                throw new ClassCastException("Presenter must be of class AddEmployeeDialogPresenter");
            }
        }

        mPresenter.attachView(this);
        mPresenter.onCreate();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mTextInputLayout = (TextInputLayout) View.inflate(getActivity(), R.layout.dialog_add_employee, null);
        AutoCompleteTextView textView = ButterKnife.findById(mTextInputLayout, R.id.employee_text_view);
        mAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, new ArrayList<>());
        textView.setAdapter(mAdapter);
        textView.setOnItemClickListener((parent, view, position, id) -> mPosition = position);
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity()).customView(mTextInputLayout, true)
                .title(R.string.title_add_employee)
                .positiveText(R.string.positive_add)
                .negativeText(android.R.string.cancel)
                .autoDismiss(false)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        if (mPosition != -1) {
                            Employee employee = mAdapter.getItem(mPosition);
                            mListener.onPositiveButtonClicked(employee.id, employee.toString(), false);
                            mPosition = -1;
                            dismiss();
                        } else {
                            mTextInputLayout.setError(getString(R.string.error_list_employee));
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
                .map(charSequence -> mPresenter.isValidEmployee(charSequence.toString()))
                .startWith(false)
                .distinctUntilChanged()
                .subscribe(aBoolean -> dialog.getActionButton(DialogAction.POSITIVE).setEnabled(aBoolean));
        return dialog;
    }

    private void initializeComponent() {
        ((RxBsuirApplication) getActivity().getApplication()).getAppComponent().inject(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
        Utils.unsubscribe(mSubscription);
    }

    @Override
    public void updateEmployeeList(List<Employee> employeeList) {
        mTextInputLayout.setErrorEnabled(false);
        mAdapter.clear();
        for (Employee employee : employeeList) {
            mAdapter.add(employee);
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
}
