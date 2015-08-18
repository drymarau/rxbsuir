package by.toggi.rxbsuir.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import javax.inject.Inject;

import butterknife.Bind;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.Utils;
import by.toggi.rxbsuir.adapter.WeekPagerAdapter;
import by.toggi.rxbsuir.component.DaggerWeekScheduleActivityComponent;
import by.toggi.rxbsuir.module.WeekScheduleActivityModule;
import by.toggi.rxbsuir.mvp.presenter.SchedulePresenter;

public class WeekScheduleActivity extends ScheduleActivity {

    @Bind(R.id.tab_layout) TabLayout mTabLayout;
    @Bind(R.id.view_pager) ViewPager mViewPager;

    @Inject WeekPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        mNavigationView.getMenu().findItem(R.id.navigation_view_week_view).setChecked(true);

        setupTabs();
        showToday();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_week_schedule;
    }

    @Override
    protected void initializeComponent() {
        DaggerWeekScheduleActivityComponent.builder()
                .weekScheduleActivityModule(new WeekScheduleActivityModule(this))
                .appComponent(((RxBsuirApplication) getApplication()).getAppComponent())
                .build().inject(this);
    }

    @Override
    protected void showToday() {
        mViewPager.setCurrentItem(Utils.getCurrentWeekNumber() - 1);
    }

    private void setupTabs() {
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setPageMargin(mPageMargin);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void showError(SchedulePresenter.Error error) {
        super.showError(error);
        mViewPager.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        super.showLoading();
        mViewPager.setVisibility(View.GONE);
    }

    @Override
    public void showContent() {
        super.showContent();
        mViewPager.setVisibility(View.VISIBLE);
        showToday();
    }
}
