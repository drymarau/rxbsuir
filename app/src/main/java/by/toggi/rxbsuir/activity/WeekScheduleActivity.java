package by.toggi.rxbsuir.activity;

import android.os.Bundle;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.Utils;
import by.toggi.rxbsuir.adapter.LessonListPagerAdapter;
import by.toggi.rxbsuir.mvp.presenter.SchedulePresenter;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class WeekScheduleActivity extends ScheduleActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupTabs();
    }

    @Override
    protected void findViews() {
        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_week_schedule;
    }

    @Override
    protected void showCurrentWeek() {
        mViewPager.setCurrentItem(Utils.getCurrentWeekNumber() + 1);
    }

    private void setupTabs() {
        mViewPager.setAdapter(new LessonListPagerAdapter(getSupportFragmentManager(), getResources().getStringArray(R.array.tabs_with_today)));
        mViewPager.setOffscreenPageLimit(mViewPager.getAdapter().getCount());
        mViewPager.setPageMargin(mPageMargin);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void showError(SchedulePresenter.Error error) {
        super.showError(error);
        mViewPager.setVisibility(View.VISIBLE);
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
    }
}
