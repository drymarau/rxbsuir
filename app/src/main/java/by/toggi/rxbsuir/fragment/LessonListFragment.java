package by.toggi.rxbsuir.fragment;

import static by.toggi.rxbsuir.mvp.presenter.LessonListPresenter.Type.TODAY;
import static by.toggi.rxbsuir.mvp.presenter.LessonListPresenter.Type.TOMORROW;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.f2prateek.rx.preferences.Preference;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.SubheaderItemDecoration;
import by.toggi.rxbsuir.activity.LessonActivity;
import by.toggi.rxbsuir.activity.ScheduleActivity;
import by.toggi.rxbsuir.adapter.LessonAdapter;
import by.toggi.rxbsuir.model.Lesson;
import by.toggi.rxbsuir.mvp.presenter.LessonListPresenter;
import by.toggi.rxbsuir.mvp.presenter.LessonListPresenter.SubgroupFilter;
import by.toggi.rxbsuir.mvp.view.LessonListView;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.AndroidEntryPoint;
import dagger.hilt.android.components.FragmentComponent;
import timber.log.Timber;

@AndroidEntryPoint
public class LessonListFragment extends Fragment
        implements LessonListView, SharedPreferences.OnSharedPreferenceChangeListener,
        LessonAdapter.OnItemClickListener {

    public static final String KEY_LAYOUT_MANAGER_STATE = "layout_manager_state";
    private static final String ARGS_VIEW_TYPE = "week_number";

    @Inject
    LessonListPresenter mPresenter;
    @Inject
    @Named(PreferenceHelper.SYNC_ID)
    Preference<String> mSyncIdPreference;
    @Inject
    @Named(PreferenceHelper.IS_GROUP_SCHEDULE)
    Preference<Boolean> mIsGroupSchedulePreference;
    @Inject
    Preference<SubgroupFilter> mSubgroupFilterPreference;
    @Inject
    SharedPreferences mSharedPreferences;
    @Inject
    @Named(PreferenceHelper.ARE_CIRCLES_COLORED)
    Preference<Boolean>
            mAreCirclesColoredPreference;

    private RecyclerView mRecyclerView;
    private TextView mEmptyState;
    private Parcelable mLayoutManagerState;
    private LessonListPresenter.Type mType;
    private LinearLayoutManager mLayoutManager;
    private LessonAdapter mAdapter;
    private final BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(@NonNull Context context, @NonNull Intent intent) {
            mPresenter.setSearchQuery(
                    intent.getCharSequenceExtra(ScheduleActivity.EXTRA_SEARCH_QUERY).toString());
        }
    };

    /**
     * Instantiates a new {@code WeekFragment}.
     *
     * @param type view type of the presenter
     * @return the week fragment
     */
    public static LessonListFragment newInstance(LessonListPresenter.Type type) {
        var args = new Bundle();
        args.putSerializable(ARGS_VIEW_TYPE, type);
        var fragment = new LessonListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        var args = getArguments();
        if (args != null) {
            mType = (LessonListPresenter.Type) args.getSerializable(ARGS_VIEW_TYPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lesson_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mEmptyState = view.findViewById(R.id.empty_state);

        var manager = getFragmentManager();
        var fragment =
                (StorageFragment) manager.findFragmentByTag(ScheduleActivity.TAG_STORAGE_FRAGMENT);

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
        mAdapter =
                new LessonAdapter(this, new ArrayList<>(), mType, mAreCirclesColoredPreference.get());

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
        if (mType == TODAY || mType == TOMORROW) {
            mPresenter.onCreate();
        }
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
        LocalBroadcastManager.getInstance(getActivity())
                .registerReceiver(mBroadcastReceiver,
                        new IntentFilter(ScheduleActivity.ACTION_SEARCH_QUERY));
    }

    @Override
    public void onPause() {
        super.onPause();
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
        try {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mBroadcastReceiver);
        } catch (IllegalArgumentException e) {
            Timber.e(e, "LocalBroadcastManager.unregisterReceiver error");
        }
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

    @Override
    public void onItemClicked(Lesson lesson) {
        LessonActivity.start(getContext(), lesson);
    }

    @dagger.Module
    @InstallIn(FragmentComponent.class)
    public static class Module {

        @Provides
        LessonListPresenter.Type provide(Fragment fragment) {
            return (LessonListPresenter.Type) fragment.getArguments().getSerializable(ARGS_VIEW_TYPE);
        }
    }
}
