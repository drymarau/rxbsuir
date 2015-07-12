package by.toggi.rxbsuir.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.WeekdayItemDecoration;
import by.toggi.rxbsuir.adapter.ScheduleAdapter;
import by.toggi.rxbsuir.component.DaggerScheduleActivityComponent;
import by.toggi.rxbsuir.fragment.DataFragment;
import by.toggi.rxbsuir.model.Schedule;
import by.toggi.rxbsuir.module.ActivityModule;
import by.toggi.rxbsuir.module.ScheduleActivityModule;
import by.toggi.rxbsuir.mvp.SchedulePresenter;
import by.toggi.rxbsuir.mvp.ScheduleView;


public class ScheduleActivity extends AppCompatActivity implements ScheduleView {

    private static final String TAG_DATA_FRAGMENT = "data_fragment";

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;
    @Bind(R.id.tab_layout) TabLayout mTabLayout;

    @Inject LinearLayoutManager mLayoutManager;
    @Inject ScheduleAdapter mAdapter;
    @Inject SchedulePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        initializeComponent();
        ButterKnife.bind(this);

        getDelegate().setSupportActionBar(mToolbar);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new WeekdayItemDecoration());

        mTabLayout.addTab(mTabLayout.newTab().setText("Week 1").setTag(1));
        mTabLayout.addTab(mTabLayout.newTab().setText("Week 2").setTag(2));
        mTabLayout.addTab(mTabLayout.newTab().setText("Week 3").setTag(3));
        mTabLayout.addTab(mTabLayout.newTab().setText("Week 4").setTag(4));

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mPresenter.setWeekNumber((int) tab.getTag());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        FragmentManager manager = getSupportFragmentManager();
        DataFragment fragment = (DataFragment) manager.findFragmentByTag(TAG_DATA_FRAGMENT);

        if (fragment == null) {
            fragment = new DataFragment();
            manager.beginTransaction().add(fragment, TAG_DATA_FRAGMENT).commit();
            fragment.setPresenter(mPresenter);
            mPresenter.attachView(this);
            mPresenter.onCreate();
        } else {
            mPresenter = (SchedulePresenter) fragment.getPresenter();
            mPresenter.attachView(this);
        }
    }

    private void initializeComponent() {
        DaggerScheduleActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .scheduleActivityModule(new ScheduleActivityModule())
                .appComponent(((RxBsuirApplication) getApplication()).getAppComponent())
                .build().inject(this);
    }

    @Override
    public void showScheduleList(List<Schedule> scheduleList) {
        mAdapter.setScheduleList(scheduleList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }
}
