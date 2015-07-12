package by.toggi.rxbsuir.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.WeekdayItemDecoration;
import by.toggi.rxbsuir.adapter.ScheduleAdapter;
import by.toggi.rxbsuir.component.DaggerScheduleFragmentComponent;
import by.toggi.rxbsuir.db.model.Lesson;
import by.toggi.rxbsuir.module.ScheduleFragmentModule;
import by.toggi.rxbsuir.mvp.SchedulePresenter;
import by.toggi.rxbsuir.mvp.ScheduleView;

public class ScheduleFragment extends Fragment implements ScheduleView {

    public static final String ARGS_WEEK_NUMBER = "week_number";
    public static final String TAG_DATA_FRAGMENT = "data_fragment";

    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;

    @Inject LinearLayoutManager mLayoutManager;
    @Inject ScheduleAdapter mAdapter;
    @Inject WeekdayItemDecoration mItemDecoration;
    @Inject SchedulePresenter mPresenter;

    private int mWeekNumber;

    public static ScheduleFragment newInstance(int weekNumber) {
        Bundle args = new Bundle();
        args.putInt(ARGS_WEEK_NUMBER, weekNumber);
        ScheduleFragment fragment = new ScheduleFragment();
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

        DaggerScheduleFragmentComponent.builder()
                .appComponent(((RxBsuirApplication) getActivity().getApplication()).getAppComponent())
                .scheduleFragmentModule(new ScheduleFragmentModule(activity))
                .build().inject(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FragmentManager manager = getActivity().getSupportFragmentManager();
        DataFragment fragment = (DataFragment) manager.findFragmentByTag(TAG_DATA_FRAGMENT);

        if (fragment == null) {
            throw new IllegalStateException("Data fragment should be already created");
        } else {
            if (fragment.getPresenter(getPresenterTag()) == null) {
                fragment.setPresenter(getPresenterTag(), mPresenter);
                mPresenter.attachView(this);
                mPresenter.onCreate();
                mPresenter.setWeekNumber(mWeekNumber);
            } else {
                mPresenter = fragment.getPresenter(getPresenterTag());
                mPresenter.attachView(this);
                mPresenter.setWeekNumber(mWeekNumber);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(mItemDecoration);

    }

    @Override
    public void showLessonList(List<Lesson> lessonList) {
        mAdapter.setLessonList(lessonList);
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

    private String getPresenterTag() {
        return "week_" + mWeekNumber;
    }
}
