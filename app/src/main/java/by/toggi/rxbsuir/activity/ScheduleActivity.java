package by.toggi.rxbsuir.activity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.Utils;
import by.toggi.rxbsuir.adapter.WeekPagerAdapter;
import by.toggi.rxbsuir.component.DaggerScheduleActivityComponent;
import by.toggi.rxbsuir.fragment.AddDialogFragment;
import by.toggi.rxbsuir.fragment.StorageFragment;
import by.toggi.rxbsuir.fragment.WeekFragment;
import by.toggi.rxbsuir.module.ActivityModule;
import by.toggi.rxbsuir.module.ScheduleActivityModule;
import by.toggi.rxbsuir.mvp.presenter.SchedulePresenter;
import by.toggi.rxbsuir.mvp.view.ScheduleView;
import icepick.Icepick;
import icepick.State;


public class ScheduleActivity extends AppCompatActivity implements ScheduleView, AddDialogFragment.OnButtonClickListener {

    public static final String KEY_GROUP_NUMBER = "selected_group_number";
    public static final String KEY_SUBGROUP_1 = "subgroup_1";
    public static final String KEY_SUBGROUP_2 = "subgroup_2";
    private static final String TAG_ADD_DIALOG = "add_dialog";

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.tab_layout) TabLayout mTabLayout;
    @Bind(R.id.view_pager) ViewPager mViewPager;
    @Bind(R.id.progress_bar) ProgressBar mProgressBar;
    @Bind(R.id.coordinator_layout) CoordinatorLayout mCoordinatorLayout;
    @Bind(R.id.fab_group) FloatingActionButton mFabGroup;
    @Bind(R.id.fab_employee) FloatingActionButton mFabEmployee;
    @Bind(R.id.floating_action_menu) FrameLayout mFloatingActionMenu;
    @Bind(R.id.floating_action_button) FloatingActionButton mFloatingActionButton;

    @Inject WeekPagerAdapter mPagerAdapter;
    @Inject SchedulePresenter mPresenter;
    @Inject SharedPreferences mSharedPreferences;

    @State CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        initializeComponent();
        ButterKnife.bind(this);
        mFloatingActionMenu.getBackground().setAlpha(0);

        addStorageFragment();

        getDelegate().setSupportActionBar(mToolbar);

        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mTabLayout.setupWithViewPager(mViewPager);

        mPresenter.attachView(this);
        mPresenter.onCreate();

        showCurrentWeek();

        Icepick.restoreInstanceState(this, savedInstanceState);
        if (mTitle == null) {
            mTitle = mSharedPreferences.getString(KEY_GROUP_NUMBER, null);
        }
        setTitle(mTitle);
    }

    private void addStorageFragment() {
        FragmentManager manager = getSupportFragmentManager();
        StorageFragment fragment = (StorageFragment) manager.findFragmentByTag(WeekFragment.TAG_STORAGE_FRAGMENT);

        if (fragment == null) {
            fragment = new StorageFragment();
            manager.beginTransaction().add(fragment, WeekFragment.TAG_STORAGE_FRAGMENT).commit();
            fragment.setPresenter(getPresenterTag(), mPresenter);
        } else {
            try {
                mPresenter = (SchedulePresenter) fragment.getPresenter(getPresenterTag());
            } catch (ClassCastException e) {
                throw new ClassCastException("Presenter must be of class SchedulePresenter");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }

    private void initializeComponent() {
        DaggerScheduleActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .scheduleActivityModule(new ScheduleActivityModule())
                .appComponent(((RxBsuirApplication) getApplication()).getAppComponent())
                .build().inject(this);
    }

    @OnClick(R.id.floating_action_button)
    public void onFloatingActionButtonClick() {
        if (mFabGroup.getVisibility() == View.VISIBLE) {
            hideFloatingActionMenu();
        } else {
            showFloatingActionMenu();
        }
    }

    private void showFloatingActionMenu() {
        mFloatingActionMenu.setClickable(true);
        mFloatingActionMenu.setOnClickListener(v -> hideFloatingActionMenu());
        ObjectAnimator.ofPropertyValuesHolder(
                mFloatingActionMenu.getBackground(),
                PropertyValuesHolder.ofInt("alpha", 255)
        ).setDuration(200).start();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mFloatingActionButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_in));
        }
        mFabGroup.show();
        mFabEmployee.show();
    }

    private void hideFloatingActionMenu() {
        mFabGroup.hide();
        mFabEmployee.hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mFloatingActionButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.rotate_out));
        }
        ObjectAnimator.ofPropertyValuesHolder(
                mFloatingActionMenu.getBackground(),
                PropertyValuesHolder.ofInt("alpha", 0)
        ).setDuration(200).start();
        mFloatingActionMenu.setClickable(false);
    }

    @OnClick(R.id.fab_employee)
    public void onFloatingActionButtonEmployeeClick() {

    }

    @OnClick(R.id.fab_group)
    public void onFloatingActionButtonGroupClick() {
        AddDialogFragment dialog = AddDialogFragment.newInstance();
        dialog.show(getSupportFragmentManager(), TAG_ADD_DIALOG);
    }

    @Override
    public void showError(Throwable throwable) {
        disableScrollFlags();
        mViewPager.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        switch (throwable.getMessage()) {
            case SchedulePresenter.ERROR_NO_GROUP:
                Snackbar.make(mCoordinatorLayout, getString(R.string.error_no_group), Snackbar.LENGTH_LONG)
                        .show();
                break;
            case SchedulePresenter.ERROR_NETWORK:
                Snackbar.make(mCoordinatorLayout, getString(R.string.error_network), Snackbar.LENGTH_LONG)
                        .setAction(R.string.action_retry, v -> mPresenter.retry())
                        .show();
                break;
            case SchedulePresenter.ERROR_EMPTY_SCHEDULE:
                Snackbar.make(mCoordinatorLayout, getString(R.string.error_empty_schedule), Snackbar.LENGTH_LONG)
                        .show();
                break;
            default:
                Snackbar.make(mCoordinatorLayout, getString(R.string.error_default), Snackbar.LENGTH_LONG)
                        .show();
                break;
        }
    }

    @Override
    public void showLoading() {
        disableScrollFlags();
        mViewPager.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showContent(int position) {
        enableScrollFlags();
        mProgressBar.setVisibility(View.GONE);
        mViewPager.setVisibility(View.VISIBLE);
    }

    @Override
    public String getPresenterTag() {
        return "schedule_presenter";
    }

    @Override
    public void onPositiveButtonClicked(String groupNumber) {
        mPresenter.setGroupNumber(groupNumber);
        setTitle(groupNumber);
        mSharedPreferences.edit().putString(KEY_GROUP_NUMBER, groupNumber).apply();
        hideFloatingActionMenu();
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getDelegate().getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Icepick.saveInstanceState(this, outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_schedule_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                mPresenter.retry();
                return true;
            case R.id.action_today:
                showCurrentWeek();
                return true;
            case R.id.action_subgroup_1:
                item.setChecked(!item.isChecked());
                mSharedPreferences.edit().putBoolean(KEY_SUBGROUP_1, item.isChecked()).apply();
                return true;
            case R.id.action_subgroup_2:
                item.setChecked(!item.isChecked());
                mSharedPreferences.edit().putBoolean(KEY_SUBGROUP_2, item.isChecked()).apply();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_subgroup_1);
        item.setChecked(mSharedPreferences.getBoolean(KEY_SUBGROUP_1, true));
        item = menu.findItem(R.id.action_subgroup_2);
        item.setChecked(mSharedPreferences.getBoolean(KEY_SUBGROUP_2, true));
        return super.onPrepareOptionsMenu(menu);
    }

    private void disableScrollFlags() {
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) mToolbar.getLayoutParams();
        params.setScrollFlags(0);
        mToolbar.setLayoutParams(params);
    }

    private void enableScrollFlags() {
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) mToolbar.getLayoutParams();
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        mToolbar.setLayoutParams(params);
    }

    private void showCurrentWeek() {
        mViewPager.setCurrentItem(Utils.getCurrentWeekNumber() - 1);
    }
}
