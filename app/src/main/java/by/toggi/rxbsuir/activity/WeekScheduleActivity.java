package by.toggi.rxbsuir.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import butterknife.BindView;
import com.f2prateek.rx.preferences.Preference;

import javax.inject.Inject;
import javax.inject.Named;

import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.Utils;
import by.toggi.rxbsuir.adapter.LessonListPagerAdapter;
import by.toggi.rxbsuir.mvp.presenter.SchedulePresenter;

public class WeekScheduleActivity extends ScheduleActivity {

    @BindView(R.id.tab_layout) TabLayout mTabLayout;
    @BindView(R.id.view_pager) ViewPager mViewPager;

    @Inject @Named(PreferenceHelper.IS_TODAY_ENABLED) Preference<Boolean> mIsTodayEnabledPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RxBsuirApplication.getAppComponent().inject(this);

        setupTabs();

        if (savedInstanceState == null && !mIsTodayEnabledPreference.get()) {
            showCurrentWeek();
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_week_schedule;
    }

    @Override
    protected void showCurrentWeek() {
        int weekNumber = Utils.getCurrentWeekNumber();
        mViewPager.setCurrentItem(mIsTodayEnabledPreference.get()
                ? weekNumber + 1
                : weekNumber - 1);
    }

    private void setupTabs() {
        mViewPager.setAdapter(new LessonListPagerAdapter(
                getSupportFragmentManager(),
                getResources().getStringArray(mIsTodayEnabledPreference.get()
                        ? R.array.tabs_with_today
                        : R.array.tabs)
        ));
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
