package by.toggi.rxbsuir.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.model.StudentGroup;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class AddGroupDialogFragment extends DialogFragment {

    private ArrayAdapter<StudentGroup> mAdapter;
    private OnButtonClickListener mListener;
    private int mPosition = -1;
    private TextInputLayout mTextInputLayout;

    public static AddGroupDialogFragment newInstance() {
        return new AddGroupDialogFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnButtonClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context + " must implement OnButtonClickListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mTextInputLayout =
                (TextInputLayout) View.inflate(getActivity(), R.layout.dialog_add_group, null);
        AutoCompleteTextView textView = mTextInputLayout.findViewById(R.id.group_number_text_view);
        mAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, new ArrayList<>());
        textView.setAdapter(mAdapter);
        textView.setOnItemClickListener((parent, view, position, id) -> mPosition = position);
        var dialog =
                new MaterialAlertDialogBuilder(getActivity())
                        .setTitle(R.string.title_add_group)
                        .setView(mTextInputLayout)
                        .setPositiveButton(R.string.positive_add, (d, which) -> {
                            if (mPosition != -1) {
                                var studentGroup = mAdapter.getItem(mPosition);
                                mListener.onPositiveButtonClicked(studentGroup.id, studentGroup.name, true);
                                mPosition = -1;
                                dismiss();
                            } else {
                                mTextInputLayout.setError(getString(R.string.error_list_group));
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, (d, which) -> d.dismiss())
                        .create();
        return dialog;
    }
}
