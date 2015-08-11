package by.toggi.rxbsuir.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.SubheaderItemDecoration;
import by.toggi.rxbsuir.adapter.LessonWithDateAdapter;
import by.toggi.rxbsuir.component.DaggerTermScheduleActivityComponent;
import by.toggi.rxbsuir.db.model.LessonWithDate;
import by.toggi.rxbsuir.module.TermScheduleActivityModule;
import by.toggi.rxbsuir.mvp.presenter.TermPresenter;
import by.toggi.rxbsuir.mvp.view.TermView;

public class TermScheduleActivity extends ScheduleActivity implements TermView, SharedPreferences.OnSharedPreferenceChangeListener {

    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;

    @Inject LinearLayoutManager mLayoutManager;
    @Inject LessonWithDateAdapter mAdapter;
    @Inject TermPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNavigationView.getMenu().findItem(R.id.navigation_view_term_view).setChecked(true);

        setupRecyclerView();

        mPresenter.attachView(this);
        mPresenter.onCreate();

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_term_schedule;
    }

    @Override
    protected void initializeComponent() {
        DaggerTermScheduleActivityComponent.builder()
                .termScheduleActivityModule(new TermScheduleActivityModule(this))
                .appComponent(((RxBsuirApplication) getApplication()).getAppComponent())
                .build().inject(this);
    }

    @Override
    protected void showToday() {
        mRecyclerView.smoothScrollToPosition(mAdapter.getTodayPosition());
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SubheaderItemDecoration(
                LayoutInflater.from(this).inflate(R.layout.list_item_subheader, mRecyclerView, false),
                getResources().getDimensionPixelSize(R.dimen.list_subheader_height)
        ));
    }

    @Override
    public void showLessonList(List<LessonWithDate> lessonWithDateList) {
        mAdapter.setLessonWithDateList(lessonWithDateList);
        mRecyclerView.setVisibility(View.VISIBLE);
        showToday();
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
    public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
        boolean isGroupSchedule = preferences.getBoolean(ScheduleActivity.KEY_IS_GROUP_SCHEDULE, true);
        switch (key) {
            case ScheduleActivity.KEY_SYNC_ID:
                mRecyclerView.setVisibility(View.GONE);
                mPresenter.setSyncId(preferences.getString(key, null), isGroupSchedule);
                break;
            case ScheduleActivity.KEY_SUBGROUP_1:
            case ScheduleActivity.KEY_SUBGROUP_2:
                boolean subgroup1 = preferences.getBoolean(ScheduleActivity.KEY_SUBGROUP_1, true);
                boolean subgroup2 = preferences.getBoolean(ScheduleActivity.KEY_SUBGROUP_2, true);
                mPresenter.setSubgroupNumber(subgroup1, subgroup2, isGroupSchedule);
                break;
        }
    }
}
