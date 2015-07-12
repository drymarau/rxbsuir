package by.toggi.rxbsuir.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.HashMap;

import by.toggi.rxbsuir.mvp.SchedulePresenter;

public class DataFragment extends Fragment {

    private final HashMap<String, SchedulePresenter> mPresenterMap = new HashMap<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public SchedulePresenter getPresenter(String tag) {
        return mPresenterMap.get(tag);
    }

    public void setPresenter(String tag, SchedulePresenter presenter) {
        mPresenterMap.put(tag, presenter);
    }
}
