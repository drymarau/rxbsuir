package by.toggi.rxbsuir.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.f2prateek.rx.preferences.Preference;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.SubheaderItemDecoration;
import by.toggi.rxbsuir.activity.ScheduleActivity;
import by.toggi.rxbsuir.adapter.LessonAdapter;
import by.toggi.rxbsuir.component.DaggerWeekFragmentComponent;
import by.toggi.rxbsuir.db.model.Lesson;
import by.toggi.rxbsuir.module.WeekFragmentModule;
import by.toggi.rxbsuir.mvp.presenter.WeekPresenter;
import by.toggi.rxbsuir.mvp.view.WeekView;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

public class WeekFragment extends Fragment implements WeekView, SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String TAG_STORAGE_FRAGMENT = "storage_fragment";
    public static final String KEY_LAYOUT_MANAGER_STATE = "layout_manager_state";
    private static final String ARGS_WEEK_NUMBER = "week_number";
    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;

    @Inject LinearLayoutManager mLayoutManager;
    @Inject LessonAdapter mAdapter;
    @Inject WeekPresenter mPresenter;
    @Inject SharedPreferences mSharedPreferences;
    @Inject Preference<String> mSyncIdPreference;
    @Inject Preference<Boolean> mIsGroupSchedulePreference;

    private Parcelable mLayoutManagerState;
    private int mWeekNumber;
    private CompositeSubscription mCompositeSubscription;

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
    public void onAttach(Context context) {
        super.onAttach(context);
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
        if (fragment.getPresenter(mPresenter.getTag()) == null) {
            fragment.setPresenter(mPresenter.getTag(), mPresenter);
        } else {
            try {
                mPresenter = (WeekPresenter) fragment.getPresenter(mPresenter.getTag());
            } catch (ClassCastException e) {
                throw new ClassCastException("Presenter must be of class WeekPresenter");
            }
        }

        mPresenter.attachView(this);
        mPresenter.onCreate();

        mRecyclerView.setVisibility(View.GONE);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SubheaderItemDecoration(
                LayoutInflater.from(getActivity()).inflate(R.layout.list_item_subheader, mRecyclerView, false),
                getResources().getDimensionPixelSize(R.dimen.list_subheader_height)
        ));
    }

    @Override
    public void showLessonList(List<Lesson> lessonList) {
        mAdapter.setLessonList(lessonList);
        if (mLayoutManagerState != null) {
            mLayoutManager.onRestoreInstanceState(mLayoutManagerState);
            mLayoutManagerState = null;
        }
        mRecyclerView.setVisibility(View.VISIBLE);
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

    /**
     * Gets week number of the fragment.
     *
     * @return the week number
     */
    public int getWeekNumber() {
        return mWeekNumber;
    }

    @Override
    public void onResume() {
        super.onResume();
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
        mCompositeSubscription = new CompositeSubscription();
        mCompositeSubscription.add(
                getSyncIdSubscription()
        );
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
        boolean isGroupSchedule = preferences.getBoolean(ScheduleActivity.KEY_IS_GROUP_SCHEDULE, true);
        switch (key) {
            case ScheduleActivity.KEY_SUBGROUP_1:
            case ScheduleActivity.KEY_SUBGROUP_2:
                boolean subgroup1 = preferences.getBoolean(ScheduleActivity.KEY_SUBGROUP_1, true);
                boolean subgroup2 = preferences.getBoolean(ScheduleActivity.KEY_SUBGROUP_2, true);
                mPresenter.setSubgroupNumber(subgroup1, subgroup2, isGroupSchedule);
                break;
        }
    }

    private Subscription getSyncIdSubscription() {
        return mSyncIdPreference.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    mRecyclerView.setVisibility(View.GONE);
                    mPresenter.setSyncId(s, mSharedPreferences.getBoolean(ScheduleActivity.KEY_IS_GROUP_SCHEDULE, true));
                });
    }
}
