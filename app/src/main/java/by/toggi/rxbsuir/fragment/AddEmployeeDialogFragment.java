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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.mvp.presenter.AddEmployeeDialogPresenter;
import by.toggi.rxbsuir.mvp.view.AddEmployeeDialogView;
import by.toggi.rxbsuir.rest.model.Employee;
import rx.android.view.ViewActions;
import rx.android.widget.WidgetObservable;

public class AddEmployeeDialogFragment extends DialogFragment implements AddEmployeeDialogView {

    @Inject AddEmployeeDialogPresenter mPresenter;

    private ArrayAdapter<Employee> mAdapter;
    private OnButtonClickListener mListener;
    private int mPosition = -1;

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
        StorageFragment fragment = (StorageFragment) manager.findFragmentByTag(WeekFragment.TAG_STORAGE_FRAGMENT);

        if (fragment == null) {
            throw new IllegalStateException("Storage fragment should already be added");
        }
        if (fragment.getPresenter(getPresenterTag()) == null) {
            fragment.setPresenter(getPresenterTag(), mPresenter);
        } else {
            try {
                mPresenter = (AddEmployeeDialogPresenter) fragment.getPresenter(getPresenterTag());
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
        TextInputLayout textInputLayout = (TextInputLayout) View.inflate(getActivity(), R.layout.dialog_add_employee, null);
        AutoCompleteTextView textView = ButterKnife.findById(textInputLayout, R.id.employee_text_view);
        mAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, new ArrayList<>());
        textView.setAdapter(mAdapter);
        textView.setOnItemClickListener((parent, view, position, id) -> mPosition = position);
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity()).customView(textInputLayout, true)
                .title(R.string.title_add_employee)
                .positiveText(R.string.positive_add)
                .negativeText(android.R.string.cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        if (mPosition != -1) {
                            mListener.onPositiveButtonClicked(mAdapter.getItem(mPosition));
                            mPosition = -1;
                        }
                    }
                })
                .build();
        // Input validation
        WidgetObservable.text(textView).map(onTextChangeEvent -> onTextChangeEvent.text().toString())
                .map(mPresenter::isValidEmployee)
                .startWith(false)
                .distinctUntilChanged()
                .subscribe(ViewActions.setEnabled(dialog.getActionButton(DialogAction.POSITIVE)));
        return dialog;
    }

    private void initializeComponent() {
        ((RxBsuirApplication) getActivity().getApplication()).getAppComponent().inject(this);
    }

    @Override
    public String getPresenterTag() {
        return "add_employee_dialog_presenter";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void updateEmployeeList(List<Employee> employeeList) {
        mAdapter.clear();
        for (Employee employee : employeeList) {
            mAdapter.add(employee);
        }
    }

    public interface OnButtonClickListener {

        void onPositiveButtonClicked(Employee employee);

    }
}
