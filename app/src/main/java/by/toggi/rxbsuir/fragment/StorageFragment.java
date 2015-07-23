package by.toggi.rxbsuir.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.HashMap;
import java.util.Map;

import by.toggi.rxbsuir.mvp.Presenter;

public class StorageFragment extends Fragment {

    private final Map<String, Presenter> mPresenterMap = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public Presenter getPresenter(String tag) {
        return mPresenterMap.get(tag);
    }

    public void setPresenter(String tag, Presenter presenter) {
        mPresenterMap.put(tag, presenter);
    }

}
