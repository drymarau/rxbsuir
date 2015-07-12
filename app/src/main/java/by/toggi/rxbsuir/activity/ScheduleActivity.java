package by.toggi.rxbsuir.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.adapter.SchedulePagerAdapter;
import by.toggi.rxbsuir.component.DaggerScheduleActivityComponent;
import by.toggi.rxbsuir.fragment.DataFragment;
import by.toggi.rxbsuir.fragment.ScheduleFragment;
import by.toggi.rxbsuir.module.ActivityModule;
import by.toggi.rxbsuir.module.ScheduleActivityModule;


public class ScheduleActivity extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.tab_layout) TabLayout mTabLayout;
    @Bind(R.id.view_pager) ViewPager mViewPager;

    @Inject SchedulePagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        FragmentManager manager = getSupportFragmentManager();
        DataFragment fragment = (DataFragment) manager.findFragmentByTag(ScheduleFragment.TAG_DATA_FRAGMENT);

        if (fragment == null) {
            fragment = new DataFragment();
            manager.beginTransaction().add(fragment, ScheduleFragment.TAG_DATA_FRAGMENT).commit();
        }

        initializeComponent();
        ButterKnife.bind(this);

        getDelegate().setSupportActionBar(mToolbar);

        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mTabLayout.setupWithViewPager(mViewPager);

    }

    private void initializeComponent() {
        DaggerScheduleActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .scheduleActivityModule(new ScheduleActivityModule())
                .appComponent(((RxBsuirApplication) getApplication()).getAppComponent())
                .build().inject(this);
    }
}
