package by.toggi.rxbsuir.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.f2prateek.rx.preferences.Preference;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;
import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.SubgroupFilter;
import by.toggi.rxbsuir.SubheaderItemDecoration;
import by.toggi.rxbsuir.activity.ScheduleActivity;
import by.toggi.rxbsuir.adapter.LessonAdapter;
import by.toggi.rxbsuir.db.model.Lesson;
import by.toggi.rxbsuir.mvp.presenter.TodayPresenter;
import by.toggi.rxbsuir.mvp.view.LessonListView;

public class TodayFragment extends Fragment implements LessonListView, SharedPreferences.OnSharedPreferenceChangeListener {

    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;
    @Bind(R.id.empty_state) TextView mEmptyState;

    @BindString(R.string.empty_state_today) String mEmptyStateText;

    @Inject TodayPresenter mPresenter;
    @Inject @Named(PreferenceHelper.SYNC_ID) Preference<String> mSyncIdPreference;
    @Inject @Named(PreferenceHelper.IS_GROUP_SCHEDULE) Preference<Boolean> mIsGroupSchedulePreference;
    @Inject Preference<SubgroupFilter> mSubgroupFilterPreference;
    @Inject SharedPreferences mSharedPreferences;

    private LessonAdapter mAdapter;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mPresenter.setSearch(intent.getCharSequenceExtra(ScheduleActivity.EXTRA_SEARCH_QUERY).toString());
        }
    };

    /**
     * Instantiates a new {@code TodayFragment}.
     *
     * @return the TodayFragment instance
     */
    public static TodayFragment newInstance() {
        return new TodayFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        ((RxBsuirApplication) getActivity().getApplication()).getAppComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lesson_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentManager manager = getFragmentManager();
        StorageFragment fragment = (StorageFragment) manager.findFragmentByTag(WeekFragment.TAG_STORAGE_FRAGMENT);

        if (fragment == null) {
            throw new IllegalStateException("Storage fragment should already be added");
        }
        if (fragment.getPresenter(mPresenter.getTag()) == null) {
            fragment.setPresenter(mPresenter.getTag(), mPresenter);
        } else {
            try {
                mPresenter = (TodayPresenter) fragment.getPresenter(mPresenter.getTag());
            } catch (ClassCastException e) {
                throw new ClassCastException("Presenter must be of class WeekPresenter");
            }
        }

        mEmptyState.setText(mEmptyStateText);

        mPresenter.attachView(this);
        mPresenter.setSyncId(mSyncIdPreference.get(), mIsGroupSchedulePreference.get());

        mAdapter = new LessonAdapter(new ArrayList<>(), true);

        mRecyclerView.setVisibility(View.GONE);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SubheaderItemDecoration(getActivity(), mRecyclerView));
    }

    @Override
    public void showLessonList(List<Lesson> lessonList) {
        mAdapter.setLessonList(lessonList);
        mRecyclerView.setVisibility(View.VISIBLE);
        mEmptyState.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyState() {
        mRecyclerView.setVisibility(View.GONE);
        mEmptyState.setVisibility(View.VISIBLE);
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
    public void onResume() {
        super.onResume();
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                mBroadcastReceiver,
                new IntentFilter(ScheduleActivity.ACTION_SEARCH_QUERY)
        );
    }

    @Override
    public void onPause() {
        super.onPause();
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        LocalBroadcastManager.getInstance(getActivity())
                .unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
        switch (key) {
            case PreferenceHelper.SYNC_ID:
                mRecyclerView.setVisibility(View.GONE);
                mPresenter.setSyncId(mSyncIdPreference.get(), mIsGroupSchedulePreference.get());
                break;
            case PreferenceHelper.SUBGROUP_FILTER:
                mPresenter.setSubgroupNumber(mSubgroupFilterPreference.get());
                break;
        }
    }
}
