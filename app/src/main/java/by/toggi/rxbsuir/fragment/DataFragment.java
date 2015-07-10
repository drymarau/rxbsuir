package by.toggi.rxbsuir.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import by.toggi.rxbsuir.mvp.Presenter;

public class DataFragment extends Fragment {

    private Presenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public Presenter getPresenter() {
        return mPresenter;
    }

    public void setPresenter(Presenter presenter) {
        mPresenter = presenter;
    }
}
