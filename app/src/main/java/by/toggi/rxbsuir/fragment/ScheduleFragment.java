package by.toggi.rxbsuir.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import by.toggi.rxbsuir.model.Schedule;
import by.toggi.rxbsuir.module.ScheduleFragmentModule;
import by.toggi.rxbsuir.mvp.SchedulePresenter;
import by.toggi.rxbsuir.mvp.ScheduleView;

public class ScheduleFragment extends Fragment implements ScheduleView {

    public static final String ARGS_WEEK_NUMBER = "week_number";

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerScheduleFragmentComponent.builder()
                .appComponent(((RxBsuirApplication) getActivity().getApplication()).getAppComponent())
                .scheduleFragmentModule(new ScheduleFragmentModule(getActivity()))
                .build().inject(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Bundle args = getArguments();
        if (args != null) {
            mWeekNumber = args.getInt(ARGS_WEEK_NUMBER);
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

        mPresenter.attachView(this);
        mPresenter.onCreate();
        mPresenter.setWeekNumber(mWeekNumber);
    }

    @Override
    public void showScheduleList(List<Schedule> scheduleList) {
        mAdapter.setScheduleList(scheduleList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        mPresenter.onDestroy();
    }
}
