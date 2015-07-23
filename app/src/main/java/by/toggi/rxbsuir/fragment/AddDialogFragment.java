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

import butterknife.ButterKnife;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.mvp.view.AddDialogView;
import rx.android.view.ViewActions;
import rx.android.widget.WidgetObservable;

public class AddDialogFragment extends DialogFragment implements AddDialogView {

    private List<String> mStudentGroupList = new ArrayList<>();

    public static AddDialogFragment newInstance() {
        return new AddDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        TextInputLayout textInputLayout = (TextInputLayout) View.inflate(getActivity(), R.layout.dialog_add_group, null);
        AutoCompleteTextView textView = ButterKnife.findById(textInputLayout, R.id.group_number_text_view);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mStudentGroupList);
        textView.setAdapter(adapter);
        MaterialDialog dialog = new MaterialDialog.Builder(getActivity()).customView(textInputLayout, true)
                .title(R.string.title_add_group)
                .positiveText(R.string.positive_add)
                .negativeText(android.R.string.cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
//                        mPresenter.setGroupNumber(textView.getText().toString());
                        Toast.makeText(getActivity(), textView.getText(), Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
        // Input validation
        WidgetObservable.text(textView).map(onTextChangeEvent -> onTextChangeEvent.text().toString())
//                .map(mPresenter::isValidGroupNumber)
                .map(s -> true)
                .startWith(false)
                .distinctUntilChanged()
                .subscribe(ViewActions.setEnabled(dialog.getActionButton(DialogAction.POSITIVE)));
        return dialog;
    }

    @Override
    public void updateStudentGroupList(List<String> studentGroupList) {
        mStudentGroupList.clear();
        mStudentGroupList.addAll(studentGroupList);
    }
}
