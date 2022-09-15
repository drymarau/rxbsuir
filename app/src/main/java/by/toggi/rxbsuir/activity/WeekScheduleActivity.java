package by.toggi.rxbsuir.activity;

import android.os.Bundle;
import android.view.View;

import androidx.viewpager.widget.ViewPager;

import com.f2prateek.rx.preferences.Preference;
import com.google.android.material.tabs.TabLayout;

import javax.inject.Inject;
import javax.inject.Named;

import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.Utils;
import by.toggi.rxbsuir.adapter.LessonListPagerAdapter;
import by.toggi.rxbsuir.dagger.PerActivity;
import by.toggi.rxbsuir.fragment.AddEmployeeDialogFragment;
import by.toggi.rxbsuir.fragment.AddGroupDialogFragment;
import by.toggi.rxbsuir.fragment.LessonListFragment;
import by.toggi.rxbsuir.mvp.presenter.SchedulePresenter;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.ContributesAndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

public class WeekScheduleActivity extends ScheduleActivity implements HasAndroidInjector {

    @Inject
    DispatchingAndroidInjector<Object> mAndroidInjector;
    @Inject
    @Named(PreferenceHelper.IS_TODAY_ENABLED)
    Preference<Boolean> mIsTodayEnabledPreference;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        setupTabs();

        if (savedInstanceState == null && !mIsTodayEnabledPreference.get()) {
            showCurrentWeek();
        }
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
        var weekNumber = Utils.getCurrentWeekNumber();
        mViewPager.setCurrentItem(mIsTodayEnabledPreference.get() ? weekNumber + 1 : weekNumber - 1);
    }

    private void setupTabs() {
        mViewPager.setAdapter(new LessonListPagerAdapter(getSupportFragmentManager(),
                getResources().getStringArray(
                        mIsTodayEnabledPreference.get() ? R.array.tabs_with_today : R.array.tabs)));
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

    @Override
    public AndroidInjector<Object> androidInjector() {
        return mAndroidInjector;
    }

    @dagger.Module(includes = {
            AddGroupDialogFragment.Module.class, AddEmployeeDialogFragment.Module.class,
            LessonListFragment.Module.class
    })
    public interface Module {

        @PerActivity
        @ContributesAndroidInjector
        WeekScheduleActivity contribute();
    }
}
