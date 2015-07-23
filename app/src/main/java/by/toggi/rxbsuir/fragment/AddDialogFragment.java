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
import by.toggi.rxbsuir.mvp.presenter.AddDialogPresenter;
import by.toggi.rxbsuir.mvp.view.AddDialogView;
import rx.android.view.ViewActions;
import rx.android.widget.WidgetObservable;

public class AddDialogFragment extends DialogFragment implements AddDialogView {

    @Inject AddDialogPresenter mPresenter;

    private ArrayAdapter<String> mAdapter;
    private OnButtonClickListener mListener;

    public static AddDialogFragment newInstance() {
        return new AddDialogFragment();
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
                mPresenter = (AddDialogPresenter) fragment.getPresenter(getPresenterTag());
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
        TextInputLayout textInputLayout = (TextInputLayout) View.inflate(getActivity(), R.layout.dialog_add_group, null);
        AutoCompleteTextView textView = ButterKnife.findById(textInputLayout, R.id.group_number_text_view);
        mAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, new ArrayList<>());
        textView.setAdapter(mAdapter);
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity()).customView(textInputLayout, true)
                .title(R.string.title_add_group)
                .positiveText(R.string.positive_add)
                .negativeText(android.R.string.cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        mListener.onPositiveButtonClicked(textView.getText().toString());
                    }
                })
                .build();
        // Input validation
        WidgetObservable.text(textView).map(onTextChangeEvent -> onTextChangeEvent.text().toString())
                .map(mPresenter::isValidGroupNumber)
                .startWith(false)
                .distinctUntilChanged()
                .subscribe(ViewActions.setEnabled(dialog.getActionButton(DialogAction.POSITIVE)));
        return dialog;
    }

    private void initializeComponent() {
        ((RxBsuirApplication) getActivity().getApplication()).getAppComponent().inject(this);
    }

    @Override
    public void updateStudentGroupList(List<String> studentGroupList) {
        mAdapter.clear();
        for (String group : studentGroupList) {
            mAdapter.add(group);
        }
    }

    @Override
    public String getPresenterTag() {
        return "add_dialog_presenter";
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    public interface OnButtonClickListener {

        void onPositiveButtonClicked(String groupNumber);

    }
}
