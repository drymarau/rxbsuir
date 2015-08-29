package by.toggi.rxbsuir.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import by.toggi.rxbsuir.SubgroupFilter;
import by.toggi.rxbsuir.SubheaderItemDecoration;
import by.toggi.rxbsuir.adapter.LessonAdapter;
import by.toggi.rxbsuir.db.model.Lesson;
import by.toggi.rxbsuir.mvp.presenter.TodayPresenter;
import by.toggi.rxbsuir.mvp.view.LessonListView;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

public class TodayFragment extends Fragment implements LessonListView {

    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;

    @Inject TodayPresenter mPresenter;
    @Inject @Named(PreferenceHelper.SYNC_ID) Preference<String> mSyncIdPreference;
    @Inject @Named(PreferenceHelper.IS_GROUP_SCHEDULE) Preference<Boolean> mIsGroupSchedulePreference;
    @Inject Preference<SubgroupFilter> mSubgroupFilterPreference;

    private CompositeSubscription mCompositeSubscription;
    private LinearLayoutManager mLayoutManager;
    private LessonAdapter mAdapter;

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

        mPresenter.attachView(this);
        mPresenter.setSyncId(mSyncIdPreference.get(), mIsGroupSchedulePreference.get());

        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new LessonAdapter(new ArrayList<>(), true);

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
        outState.putParcelable(WeekFragment.KEY_LAYOUT_MANAGER_STATE, mLayoutManager.onSaveInstanceState());
    }

    @Override
    public void onResume() {
        super.onResume();
        mCompositeSubscription = new CompositeSubscription(
                getSyncIdSubscription(),
                getSubgroupFilterSubscription()
        );
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mCompositeSubscription != null && mCompositeSubscription.hasSubscriptions()) {
            mCompositeSubscription.unsubscribe();
        }
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
                .subscribe(filter -> mPresenter.setSubgroupNumber(filter, mIsGroupSchedulePreference.get()));
    }
}
