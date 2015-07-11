package by.toggi.rxbsuir.activity;

import android.os.Bundle;
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
