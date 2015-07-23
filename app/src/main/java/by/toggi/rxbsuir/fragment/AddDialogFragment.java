package by.toggi.rxbsuir.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

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

    public static AddDialogFragment newInstance() {
        return new AddDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initializeComponent();

        mPresenter.attachView(this);
        mPresenter.onCreate();

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
                        Toast.makeText(getActivity(), textView.getText(), Toast.LENGTH_SHORT).show();
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
}
