package by.toggi.rxbsuir.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.activity.ScheduleActivity;
import by.toggi.rxbsuir.adapter.LessonAdapter;
import by.toggi.rxbsuir.component.DaggerWeekFragmentComponent;
import by.toggi.rxbsuir.db.model.Lesson;
import by.toggi.rxbsuir.module.WeekFragmentModule;
import by.toggi.rxbsuir.mvp.presenter.WeekPresenter;
import by.toggi.rxbsuir.mvp.view.WeekView;

public class WeekFragment extends Fragment implements WeekView, SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String TAG_STORAGE_FRAGMENT = "storage_fragment";
    public static final String ARGS_WEEK_NUMBER = "week_number";
    private static final String KEY_LAYOUT_MANAGER_STATE = "layout_manager_state";

    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;

    @Inject LinearLayoutManager mLayoutManager;
    @Inject LessonAdapter mAdapter;
    @Inject WeekPresenter mPresenter;
    @Inject SharedPreferences mSharedPreferences;

    private Parcelable mLayoutManagerState;
    private int mWeekNumber;

    /**
     * Instantiates a new {@code WeekFragment}.
     *
     * @param weekNumber the week number
     * @return the week fragment
     */
    public static WeekFragment newInstance(int weekNumber) {
        if (weekNumber < 1 || weekNumber > 4) {
            throw new IllegalArgumentException("WeekFragment can only accept weekNumber from 1 to 4. Supplied weekNumber: " + weekNumber);
        }
        Bundle args = new Bundle();
        args.putInt(ARGS_WEEK_NUMBER, weekNumber);
        WeekFragment fragment = new WeekFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Bundle args = getArguments();
        if (args != null) {
            mWeekNumber = args.getInt(ARGS_WEEK_NUMBER);
        }

        DaggerWeekFragmentComponent.builder()
                .appComponent(((RxBsuirApplication) getActivity().getApplication()).getAppComponent())
                .weekFragmentModule(new WeekFragmentModule(this))
                .build().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_week, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentManager manager = getFragmentManager();
        StorageFragment fragment = (StorageFragment) manager.findFragmentByTag(TAG_STORAGE_FRAGMENT);

        if (fragment == null) {
            throw new IllegalStateException("Storage fragment should already be added");
        }
        if (fragment.getPresenter(getPresenterTag()) == null) {
            fragment.setPresenter(getPresenterTag(), mPresenter);
        } else {
            try {
                mPresenter = (WeekPresenter) fragment.getPresenter(getPresenterTag());
            } catch (ClassCastException e) {
                throw new ClassCastException("Presenter must be of class WeekPresenter");
            }
        }

        mPresenter.attachView(this);
        mPresenter.onCreate();

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void showLessonList(List<Lesson> lessonList) {
        ProgressBar progressBar = ButterKnife.findById(getActivity(), R.id.progress_bar);
        TextView textView = ButterKnife.findById(getActivity(), R.id.error_text_view);
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
        if (textView != null) {
            textView.setVisibility(View.GONE);
        }
        mAdapter.setLessonList(lessonList);
        if (mLayoutManagerState != null) {
            mLayoutManager.onRestoreInstanceState(mLayoutManagerState);
            mLayoutManagerState = null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_LAYOUT_MANAGER_STATE, mLayoutManager.onSaveInstanceState());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mLayoutManagerState = savedInstanceState.getParcelable(KEY_LAYOUT_MANAGER_STATE);
        }
    }

    private String getPresenterTag() {
        return "week_" + mWeekNumber;
    }

    /**
     * Gets week number of the fragment.
     *
     * @return the week number
     */
    public int getWeekNumber() {
        return mWeekNumber;
    }

    /**
     * Gets group number from shared preferences.
     *
     * @return the group number, {@value null} if no group
     */
    public String getGroupNumber() {
        return PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString(ScheduleActivity.KEY_GROUP_NUMBER, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        mPresenter.setGroupNumber(sharedPreferences.getString(ScheduleActivity.KEY_GROUP_NUMBER, null));
    }
}
