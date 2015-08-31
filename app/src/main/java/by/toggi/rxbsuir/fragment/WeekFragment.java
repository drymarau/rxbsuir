package by.toggi.rxbsuir.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import butterknife.BindString;
import butterknife.ButterKnife;
import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.SubgroupFilter;
import by.toggi.rxbsuir.SubheaderItemDecoration;
import by.toggi.rxbsuir.Utils;
import by.toggi.rxbsuir.activity.ScheduleActivity;
import by.toggi.rxbsuir.adapter.LessonAdapter;
import by.toggi.rxbsuir.component.DaggerWeekFragmentComponent;
import by.toggi.rxbsuir.db.model.Lesson;
import by.toggi.rxbsuir.module.WeekFragmentModule;
import by.toggi.rxbsuir.mvp.presenter.WeekPresenter;
import by.toggi.rxbsuir.mvp.view.LessonListView;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;
import timber.log.Timber;

public class WeekFragment extends Fragment implements LessonListView {

    public static final String TAG_STORAGE_FRAGMENT = "storage_fragment";
    public static final String KEY_LAYOUT_MANAGER_STATE = "layout_manager_state";
    private static final String ARGS_WEEK_NUMBER = "week_number";

    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;
    @Bind(R.id.empty_state) TextView mEmptyState;

    @BindString(R.string.empty_state_week) String mEmptyStateText;

    @Inject WeekPresenter mPresenter;
    @Inject @Named(PreferenceHelper.SYNC_ID) Preference<String> mSyncIdPreference;
    @Inject @Named(PreferenceHelper.IS_GROUP_SCHEDULE) Preference<Boolean> mIsGroupSchedulePreference;
    @Inject Preference<SubgroupFilter> mSubgroupFilterPreference;

    private Parcelable mLayoutManagerState;
    private int mWeekNumber;
    private CompositeSubscription mCompositeSubscription;
    private LinearLayoutManager mLayoutManager;
    private LessonAdapter mAdapter;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Timber.d(intent.getCharSequenceExtra(ScheduleActivity.EXTRA_SEARCH_QUERY).toString());
            mPresenter.setSubjectFilter(intent.getCharSequenceExtra(ScheduleActivity.EXTRA_SEARCH_QUERY).toString());
        }
    };

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
                .weekFragmentModule(new WeekFragmentModule(mWeekNumber))
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

        mEmptyState.setText(mEmptyStateText);

        mPresenter.attachView(this);
        mPresenter.setSyncId(mSyncIdPreference.get(), mIsGroupSchedulePreference.get());

        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new LessonAdapter(new ArrayList<>(), false);

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
        mCompositeSubscription = new CompositeSubscription(
                getSyncIdSubscription(),
                getSubgroupFilterSubscription()
        );
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                mBroadcastReceiver,
                new IntentFilter(ScheduleActivity.ACTION_SEARCH_QUERY)
        );
    }

    @Override
    public void onPause() {
        super.onPause();
        Utils.unsubscribeComposite(mCompositeSubscription);
        LocalBroadcastManager.getInstance(getActivity())
                .unregisterReceiver(mBroadcastReceiver);
    }

    private Subscription getSyncIdSubscription() {
        return mSyncIdPreference.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    mRecyclerView.setVisibility(View.GONE);
                    mPresenter.setSyncId(s, mIsGroupSchedulePreference.get());
                });
    }

    private Subscription getSubgroupFilterSubscription() {
        return mSubgroupFilterPreference.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mPresenter::setSubgroupNumber);
    }
}
