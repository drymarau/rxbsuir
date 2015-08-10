package by.toggi.rxbsuir.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import butterknife.Bind;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.Utils;
import by.toggi.rxbsuir.component.DaggerWeekScheduleActivityComponent;
import by.toggi.rxbsuir.module.ActivityModule;
import by.toggi.rxbsuir.module.WeekScheduleActivityModule;
import by.toggi.rxbsuir.mvp.presenter.SchedulePresenter;

public class WeekScheduleActivity extends ScheduleActivity {

    @Bind(R.id.tab_layout) TabLayout mTabLayout;
    @Bind(R.id.view_pager) ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                .activityModule(new ActivityModule(this))
                .weekScheduleActivityModule(new WeekScheduleActivityModule())
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
    public void showContent(int position) {
        super.showContent(position);
        mViewPager.setVisibility(View.VISIBLE);
    }
}
