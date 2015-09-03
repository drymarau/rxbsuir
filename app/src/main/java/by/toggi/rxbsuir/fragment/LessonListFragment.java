package by.toggi.rxbsuir.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
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
import butterknife.ButterKnife;
import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.SubheaderItemDecoration;
import by.toggi.rxbsuir.activity.ScheduleActivity;
import by.toggi.rxbsuir.adapter.LessonAdapter;
import by.toggi.rxbsuir.component.DaggerLessonListFragmentComponent;
import by.toggi.rxbsuir.db.model.Lesson;
import by.toggi.rxbsuir.module.LessonListFragmentModule;
import by.toggi.rxbsuir.mvp.presenter.LessonListPresenter;
import by.toggi.rxbsuir.mvp.presenter.LessonListPresenter.SubgroupFilter;
import by.toggi.rxbsuir.mvp.view.LessonListView;

public class LessonListFragment extends Fragment implements LessonListView, SharedPreferences.OnSharedPreferenceChangeListener {

    public static final String KEY_LAYOUT_MANAGER_STATE = "layout_manager_state";
    private static final String ARGS_VIEW_TYPE = "week_number";

    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;
    @Bind(R.id.empty_state) TextView mEmptyState;

    @Inject LessonListPresenter mPresenter;
    @Inject @Named(PreferenceHelper.SYNC_ID) Preference<String> mSyncIdPreference;
    @Inject @Named(PreferenceHelper.IS_GROUP_SCHEDULE) Preference<Boolean> mIsGroupSchedulePreference;
    @Inject Preference<SubgroupFilter> mSubgroupFilterPreference;
    @Inject SharedPreferences mSharedPreferences;

    private Parcelable mLayoutManagerState;
    private LessonListPresenter.Type mType;
    private LinearLayoutManager mLayoutManager;
    private LessonAdapter mAdapter;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mPresenter.setSearchQuery(intent.getCharSequenceExtra(ScheduleActivity.EXTRA_SEARCH_QUERY).toString());
        }
    };

    /**
     * Instantiates a new {@code WeekFragment}.
     *
     * @param type view type of the presenter
     * @return the week fragment
     */
    public static LessonListFragment newInstance(LessonListPresenter.Type type) {
        Bundle args = new Bundle();
        args.putSerializable(ARGS_VIEW_TYPE, type);
        LessonListFragment fragment = new LessonListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle args = getArguments();
        if (args != null) {
            mType = (LessonListPresenter.Type) args.getSerializable(ARGS_VIEW_TYPE);
        }

        DaggerLessonListFragmentComponent.builder()
                .appComponent(((RxBsuirApplication) getActivity().getApplication()).getAppComponent())
                .lessonListFragmentModule(new LessonListFragmentModule(mType))
                .build().inject(this);
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
        StorageFragment fragment = (StorageFragment) manager.findFragmentByTag(ScheduleActivity.TAG_STORAGE_FRAGMENT);

        if (fragment == null) {
            throw new IllegalStateException("Storage fragment should already be added");
        }
        if (fragment.getPresenter(mPresenter.getTag()) == null) {
            fragment.setPresenter(mPresenter.getTag(), mPresenter);
        } else {
            try {
                mPresenter = (LessonListPresenter) fragment.getPresenter(mPresenter.getTag());
            } catch (ClassCastException e) {
                throw new ClassCastException("Presenter must be of class WeekPresenter");
            }
        }

        switch (mType) {
            case TODAY:
                mEmptyState.setText(R.string.empty_state_today);
                break;
            case TOMORROW:
                mEmptyState.setText(R.string.empty_state_tomorrow);
                break;
            default:
                mEmptyState.setText(R.string.empty_state_week);
                break;
        }

        mPresenter.attachView(this);
        mPresenter.setSyncId(mSyncIdPreference.get(), mIsGroupSchedulePreference.get());

        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new LessonAdapter(new ArrayList<>(), mType);

        mRecyclerView.setVisibility(View.GONE);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SubheaderItemDecoration(getActivity(), mRecyclerView));
    }

    @Override
    public void showLessonList(List<Lesson> lessonList) {
        mAdapter.setLessonList(lessonList);
        if (mLayoutManagerState != null) {
            mLayoutManager.onRestoreInstanceState(mLayoutManagerState);
            mLayoutManagerState = null;
        }
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
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        switch (key) {
            case PreferenceHelper.SYNC_ID:
                mRecyclerView.setVisibility(View.GONE);
                mPresenter.setSyncId(mSyncIdPreference.get(), mIsGroupSchedulePreference.get());
                break;
            case PreferenceHelper.SUBGROUP_FILTER:
                mPresenter.setSubgroupFilter(mSubgroupFilterPreference.get());
                break;
        }
    }
}
