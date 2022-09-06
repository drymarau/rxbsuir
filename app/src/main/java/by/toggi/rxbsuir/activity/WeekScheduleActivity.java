package by.toggi.rxbsuir.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import butterknife.BindView;
import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.Utils;
import by.toggi.rxbsuir.adapter.LessonListPagerAdapter;
import by.toggi.rxbsuir.dagger.PerActivity;
import by.toggi.rxbsuir.fragment.AddEmployeeDialogFragment;
import by.toggi.rxbsuir.fragment.AddGroupDialogFragment;
import by.toggi.rxbsuir.fragment.LessonListFragment;
import by.toggi.rxbsuir.mvp.presenter.SchedulePresenter;
import com.f2prateek.rx.preferences.Preference;
import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.ContributesAndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

import javax.inject.Inject;
import javax.inject.Named;

public class WeekScheduleActivity extends ScheduleActivity implements HasAndroidInjector {

  @BindView(R.id.tab_layout) TabLayout mTabLayout;
  @BindView(R.id.view_pager) ViewPager mViewPager;

  @Inject DispatchingAndroidInjector<Object> mAndroidInjector;
  @Inject @Named(PreferenceHelper.IS_TODAY_ENABLED) Preference<Boolean> mIsTodayEnabledPreference;

  @Override protected void onCreate(Bundle savedInstanceState) {
    AndroidInjection.inject(this);
    super.onCreate(savedInstanceState);

    setupTabs();

    if (savedInstanceState == null && !mIsTodayEnabledPreference.get()) {
      showCurrentWeek();
    }
  }

  @Override protected int getLayoutRes() {
    return R.layout.activity_week_schedule;
  }

  @Override protected void showCurrentWeek() {
    int weekNumber = Utils.getCurrentWeekNumber();
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

  @Override public void showError(SchedulePresenter.Error error) {
    super.showError(error);
    mViewPager.setVisibility(View.VISIBLE);
  }

  @Override public void showLoading() {
    super.showLoading();
    mViewPager.setVisibility(View.GONE);
  }

  @Override public void showContent() {
    super.showContent();
    mViewPager.setVisibility(View.VISIBLE);
  }

  @Override public AndroidInjector<Object> androidInjector() {
    return mAndroidInjector;
  }

  @dagger.Module(includes = {
      AddGroupDialogFragment.Module.class, AddEmployeeDialogFragment.Module.class,
      LessonListFragment.Module.class
  }) public interface Module {

    @PerActivity @ContributesAndroidInjector WeekScheduleActivity contribute();
  }
}
