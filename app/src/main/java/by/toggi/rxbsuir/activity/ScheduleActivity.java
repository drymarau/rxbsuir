package by.toggi.rxbsuir.activity;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.f2prateek.rx.preferences.Preference;

import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.Bind;
import butterknife.BindDimen;
import butterknife.BindString;
import butterknife.ButterKnife;
import butterknife.OnClick;
import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.fragment.AddEmployeeDialogFragment;
import by.toggi.rxbsuir.fragment.AddGroupDialogFragment;
import by.toggi.rxbsuir.fragment.OnButtonClickListener;
import by.toggi.rxbsuir.fragment.StorageFragment;
import by.toggi.rxbsuir.fragment.WeekFragment;
import by.toggi.rxbsuir.mvp.presenter.NavigationDrawerPresenter;
import by.toggi.rxbsuir.mvp.presenter.SchedulePresenter;
import by.toggi.rxbsuir.mvp.view.NavigationDrawerView;
import by.toggi.rxbsuir.mvp.view.ScheduleView;
import icepick.Icepick;
import icepick.State;

import static by.toggi.rxbsuir.mvp.presenter.SchedulePresenter.Error;


public abstract class ScheduleActivity extends AppCompatActivity implements ScheduleView, NavigationDrawerView, NavigationView.OnNavigationItemSelectedListener, OnButtonClickListener {

    private static final String TAG_ADD_GROUP_DIALOG = "add_group_dialog";
    private static final String TAG_ADD_EMPLOYEE_DIALOG = "add_employee_dialog";

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.progress_bar) ProgressBar mProgressBar;
    @Bind(R.id.coordinator_layout) CoordinatorLayout mCoordinatorLayout;
    @Bind(R.id.fab_group) FloatingActionButton mFabGroup;
    @Bind(R.id.fab_employee) FloatingActionButton mFabEmployee;
    @Bind(R.id.fam) RelativeLayout mFloatingActionMenu;
    @Bind(R.id.fab) FloatingActionButton mFloatingActionButton;
    @Bind(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @Bind(R.id.navigation_view) NavigationView mNavigationView;

    @BindDimen(R.dimen.view_pager_page_margin) int mPageMargin;

    @BindString(R.string.intent_feedback) String mSendFeedbackTitle;
    @BindString(R.string.email_feedback) String mFeedbackEmail;
    @BindString(R.string.subject_feedback) String mFeedbackSubject;

    @Inject SchedulePresenter mSchedulePresenter;
    @Inject NavigationDrawerPresenter mDrawerPresenter;
    @Inject SharedPreferences mSharedPreferences;
    @Inject @Named(PreferenceHelper.KEY_IS_DARK_THEME) boolean mIsDarkTheme;
    @Inject Preference<String> mSyncId;
    @Inject Preference<Boolean> mIsGroupSchedule;

    @State CharSequence mTitle;
    @State int mItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initializeComponent();

        setTheme(mIsDarkTheme ? R.style.AppTheme_Drawer_Dark : R.style.AppTheme_Drawer_Light);

        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());

        ButterKnife.bind(this);
        mFloatingActionMenu.getBackground().setAlpha(0);

        addStorageFragment();

        setupNavigationView();

        mSchedulePresenter.attachView(this);
        mSchedulePresenter.onCreate();

        mDrawerPresenter.attachView(this);
        mDrawerPresenter.onCreate();

        Icepick.restoreInstanceState(this, savedInstanceState);
        setupTitle();

    }

    @LayoutRes
    protected abstract int getLayoutRes();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSchedulePresenter.onDestroy();
        mDrawerPresenter.onDestroy();
    }

    protected abstract void initializeComponent();

    @OnClick(R.id.fab)
    public void onFloatingActionButtonClick() {
        if (mFabGroup.getVisibility() == View.VISIBLE) {
            hideFloatingActionMenu();
        } else {
            showFloatingActionMenu();
        }
    }

    @OnClick(R.id.fab_employee)
    public void onFloatingActionButtonEmployeeClick() {
        AddEmployeeDialogFragment dialog = AddEmployeeDialogFragment.newInstance();
        dialog.show(getSupportFragmentManager(), TAG_ADD_EMPLOYEE_DIALOG);
    }

    @OnClick(R.id.fab_group)
    public void onFloatingActionButtonGroupClick() {
        AddGroupDialogFragment dialog = AddGroupDialogFragment.newInstance();
        dialog.show(getSupportFragmentManager(), TAG_ADD_GROUP_DIALOG);
    }

    @Override
    public void showError(Error error) {
        mProgressBar.setVisibility(View.GONE);
        resetSyncId();
        switch (error) {
            case NETWORK:
                Snackbar.make(mCoordinatorLayout, getString(R.string.error_network), Snackbar.LENGTH_LONG)
                        .setAction(R.string.action_retry, v -> mSchedulePresenter.retry())
                        .show();
                break;
            case EMPTY_SCHEDULE:
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
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showContent() {
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onPositiveButtonClicked(int id, String name, boolean isGroupSchedule) {
        hideFloatingActionMenu();
        selectGroupOrEmployee(id, name, isGroupSchedule);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        if (mTitle != null) {
            mSharedPreferences.edit().putString(PreferenceHelper.KEY_TITLE, title.toString()).apply();
        }
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
                mSchedulePresenter.retry();
                return true;
            case R.id.action_today:
                showToday();
                return true;
            case R.id.action_subgroup_1:
                item.setChecked(!item.isChecked());
                mSharedPreferences.edit().putBoolean(PreferenceHelper.KEY_SUBGROUP_1, item.isChecked()).apply();
                return true;
            case R.id.action_subgroup_2:
                item.setChecked(!item.isChecked());
                mSharedPreferences.edit().putBoolean(PreferenceHelper.KEY_SUBGROUP_2, item.isChecked()).apply();
                return true;
            case R.id.action_delete:
                mSchedulePresenter.remove(mSyncId.get(), mIsGroupSchedule.get());
                resetSyncId();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void resetSyncId() {
        mSyncId.set(null);
        setTitle(R.string.app_name);
        supportInvalidateOptionsMenu();
    }

    protected abstract void showToday();

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.setGroupVisible(R.id.group_items, isMenuItemEnabled());
        MenuItem item = menu.findItem(R.id.action_subgroup_1);
        item.setVisible(isMenuItemEnabled());
        item.setChecked(mSharedPreferences.getBoolean(PreferenceHelper.KEY_SUBGROUP_1, true));
        item = menu.findItem(R.id.action_subgroup_2);
        item.setVisible(isMenuItemEnabled());
        item.setChecked(mSharedPreferences.getBoolean(PreferenceHelper.KEY_SUBGROUP_2, true));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void updateGroupList(Map<Integer, String> groupMap) {
        Menu menu = mNavigationView.getMenu();
        MenuItem groupHeader = menu.findItem(R.id.navigation_view_groups_header);
        if (groupMap.size() > 0) {
            groupHeader.setVisible(true);
            groupHeader.getSubMenu().clear();
            for (int id : groupMap.keySet()) {
                groupHeader.getSubMenu().add(R.id.navigation_view_groups, id, Menu.NONE, groupMap.get(id));
            }
            MenuItem item = menu.getItem(0);
            item.setTitle(item.getTitle());
        } else {
            groupHeader.setVisible(false);
        }
    }

    @Override
    public void updateEmployeeList(Map<Integer, String> employeeMap) {
        Menu menu = mNavigationView.getMenu();
        MenuItem employeeHeader = menu.findItem(R.id.navigation_view_employees_header);
        if (employeeMap.size() > 0) {
            employeeHeader.setVisible(true);
            employeeHeader.getSubMenu().clear();
            for (int id : employeeMap.keySet()) {
                employeeHeader.getSubMenu().add(R.id.navigation_view_employees, id, Menu.NONE, employeeMap.get(id));
            }
            MenuItem item = menu.getItem(0);
            item.setTitle(item.getTitle());
        } else {
            employeeHeader.setVisible(false);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (mItemId != menuItem.getItemId()) {
            switch (menuItem.getGroupId()) {
                case R.id.navigation_view_groups:
                    selectGroupOrEmployee(itemId, menuItem.getTitle().toString(), true);
                    break;
                case R.id.navigation_view_employees:
                    selectGroupOrEmployee(itemId, menuItem.getTitle().toString(), false);
                    break;
            }
            if (itemId == R.id.navigation_view_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
            }
            if (itemId == R.id.navigation_view_feedback) {
                Intent feedbackIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", mFeedbackEmail, null));
                feedbackIntent.putExtra(Intent.EXTRA_SUBJECT, mFeedbackSubject);
                startActivity(Intent.createChooser(feedbackIntent, mSendFeedbackTitle));
                return true;
            }
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void selectGroupOrEmployee(int id, String s, boolean isGroupSchedule) {
        mItemId = id;
        mSyncId.set(isGroupSchedule ? s : String.valueOf(id));
        mIsGroupSchedule.set(isGroupSchedule);
        mSchedulePresenter.setSyncId(mSyncId.get(), mIsGroupSchedule.get());
        setTitle(s);
        supportInvalidateOptionsMenu();
    }

    private void setupTitle() {
        if (mTitle == null) {
            CharSequence title = getDelegate().getSupportActionBar().getTitle();
            if (title != null) {
                mTitle = mSharedPreferences.getString(PreferenceHelper.KEY_TITLE, title.toString());
            }
        }
        setTitle(mTitle);
    }

    private void setupNavigationView() {
        getDelegate().setSupportActionBar(mToolbar);
        getDelegate().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.ic_action_navigation_menu);
        mToolbar.setNavigationOnClickListener(v -> mDrawerLayout.openDrawer(GravityCompat.START));
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    private boolean isMenuItemEnabled() {
        return mSyncId != null;
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

    private void addStorageFragment() {
        FragmentManager manager = getSupportFragmentManager();
        StorageFragment fragment = (StorageFragment) manager.findFragmentByTag(WeekFragment.TAG_STORAGE_FRAGMENT);

        if (fragment == null) {
            fragment = new StorageFragment();
            manager.beginTransaction().add(fragment, WeekFragment.TAG_STORAGE_FRAGMENT).commit();
            fragment.setPresenter(mSchedulePresenter.getTag(), mSchedulePresenter);
        } else {
            try {
                mSchedulePresenter = (SchedulePresenter) fragment.getPresenter(mSchedulePresenter.getTag());
            } catch (ClassCastException e) {
                throw new ClassCastException("Presenter must be of class SchedulePresenter");
            }
        }
    }

    private void showFloatingActionMenu() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, GravityCompat.START);
        mFloatingActionMenu.setClickable(true);
        mFloatingActionMenu.setOnClickListener(v -> hideFloatingActionMenu());
        ObjectAnimator.ofPropertyValuesHolder(
                mFloatingActionMenu.getBackground(),
                PropertyValuesHolder.ofInt("alpha", 255)
        ).setDuration(200).start();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mFloatingActionButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fab_rotate_in));
        }
        mFabGroup.show();
        mFabEmployee.show();
    }

    private void hideFloatingActionMenu() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, GravityCompat.START);
        mFabGroup.hide();
        mFabEmployee.hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mFloatingActionButton.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fab_rotate_out));
        }
        ObjectAnimator.ofPropertyValuesHolder(
                mFloatingActionMenu.getBackground(),
                PropertyValuesHolder.ofInt("alpha", 0)
        ).setDuration(200).start();
        mFloatingActionMenu.setClickable(false);
    }
}
