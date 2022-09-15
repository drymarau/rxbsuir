package by.toggi.rxbsuir.activity;

import static by.toggi.rxbsuir.mvp.presenter.SchedulePresenter.Error;

import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.f2prateek.rx.preferences.Preference;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.jakewharton.rxbinding.support.v7.widget.RxSearchView;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.time.LocalTime;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.RateAppDialog;
import by.toggi.rxbsuir.Utils;
import by.toggi.rxbsuir.fragment.AddEmployeeDialogFragment;
import by.toggi.rxbsuir.fragment.AddGroupDialogFragment;
import by.toggi.rxbsuir.fragment.OnButtonClickListener;
import by.toggi.rxbsuir.fragment.StorageFragment;
import by.toggi.rxbsuir.mvp.presenter.LessonListPresenter.SubgroupFilter;
import by.toggi.rxbsuir.mvp.presenter.NavigationDrawerPresenter;
import by.toggi.rxbsuir.mvp.presenter.SchedulePresenter;
import by.toggi.rxbsuir.mvp.view.NavigationDrawerView;
import by.toggi.rxbsuir.mvp.view.ScheduleView;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import timber.log.Timber;

public abstract class ScheduleActivity extends RxAppCompatActivity
        implements ScheduleView, NavigationDrawerView, NavigationView.OnNavigationItemSelectedListener,
        OnButtonClickListener {

    public static final String ACTION_SEARCH_QUERY = "by.toggi.rxbsuir.action.search_query";
    public static final String EXTRA_SEARCH_QUERY = "by.toggi.rxbsuir.extra.search_query";

    public static final String TAG_STORAGE_FRAGMENT = "storage_fragment";

    private static final String TAG_ADD_GROUP_DIALOG = "add_group_dialog";
    private static final String TAG_ADD_EMPLOYEE_DIALOG = "add_employee_dialog";
    private static final long ANIMATION_DURATION = 250;

    @Inject
    SchedulePresenter mSchedulePresenter;
    @Inject
    NavigationDrawerPresenter mDrawerPresenter;
    @Inject
    SharedPreferences mSharedPreferences;
    @Inject
    @Named(PreferenceHelper.IS_FAM_ENABLED)
    boolean mIsFamEnabled;
    @Inject
    @Named(PreferenceHelper.SYNC_ID)
    Preference<String> mSyncIdPreference;
    @Inject
    @Named(PreferenceHelper.TITLE)
    Preference<String> mTitlePreference;
    @Inject
    @Named(PreferenceHelper.IS_GROUP_SCHEDULE)
    Preference<Boolean> mIsGroupSchedulePreference;
    @Inject
    Preference<Integer> mItemIdPreference;
    @Inject
    Preference<SubgroupFilter> mSubgroupFilterPreference;
    @Inject
    @Named(PreferenceHelper.FAVORITE_SYNC_ID)
    Preference<String> mFavoriteSyncIdPreference;
    @Inject
    @Named(PreferenceHelper.FAVORITE_IS_GROUP_SCHEDULE)
    Preference<Boolean>
            mFavoriteIsGroupSchedulePreference;
    @Inject
    @Named(PreferenceHelper.FAVORITE_TITLE)
    Preference<String> mFavoriteTitlePreference;
    @Inject
    Preference<LocalTime> mLocalTimePreference;
    @Inject
    @Named(PreferenceHelper.NIGHT_MODE)
    Preference<String> mNightModePreference;

    private Toolbar mToolbar;
    private ProgressBar mProgressBar;
    private CoordinatorLayout mCoordinatorLayout;
    private FloatingActionButton mFabGroup;
    private FloatingActionButton mFabEmployee;
    private RelativeLayout mFloatingActionMenu;
    private FloatingActionButton mFloatingActionButton;
    @Nullable
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    protected int mPageMargin;

    private String mSendFeedbackTitle;
    private String mFeedbackEmail;
    private String mFeedbackSubject;
    private String mTitleFormat;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(@NonNull Context context, @NonNull Intent intent) {
            ScheduleActivity.this.supportInvalidateOptionsMenu();
        }
    };
    private Subscription mSearchViewSubscription;
    private LocalBroadcastManager mLocalBroadcastManager;
    private ValueAnimator mFabValueAnimator;
    private ValueAnimator mFamBackgroundValueAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());

        mToolbar = findViewById(R.id.toolbar);
        mProgressBar = findViewById(R.id.progress_bar);
        mCoordinatorLayout = findViewById(R.id.coordinator_layout);
        mFabGroup = findViewById(R.id.fab_group);
        mFabEmployee = findViewById(R.id.fab_employee);
        mFloatingActionMenu = findViewById(R.id.fam);
        mFloatingActionButton = findViewById(R.id.fab);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.navigation_view);
        mPageMargin = getResources().getDimensionPixelSize(R.dimen.view_pager_page_margin);
        mSendFeedbackTitle = getString(R.string.intent_feedback);
        mFeedbackEmail = getString(R.string.email_feedback);
        mFeedbackSubject = getString(R.string.subject_feedback);
        mTitleFormat = getString(R.string.title_format);
        findViews();

        mFabGroup.setOnClickListener(view -> {
            var dialog = AddGroupDialogFragment.newInstance();
            dialog.show(getSupportFragmentManager(), TAG_ADD_GROUP_DIALOG);
        });
        mFabEmployee.setOnClickListener(view -> {
            var dialog = AddEmployeeDialogFragment.newInstance();
            dialog.show(getSupportFragmentManager(), TAG_ADD_EMPLOYEE_DIALOG);
        });
        mFloatingActionButton.setOnClickListener(view -> {
            if (Utils.hasNetworkConnection(view.getContext())) {
                toggleFloatingActionMenu(mFabGroup.getVisibility() != View.VISIBLE);
            } else {
                Snackbar.make(mCoordinatorLayout, R.string.error_network, Snackbar.LENGTH_SHORT).show();
            }
        });

        if (!mIsFamEnabled) {
            mFloatingActionMenu.setVisibility(View.GONE);
        }

        mFloatingActionMenu.getBackground().setAlpha(0);

        addStorageFragment();

        setupNavigationView();

        mDrawerPresenter.attachView(this);
        mDrawerPresenter.onCreate();

        mSchedulePresenter.attachView(this);

        if (savedInstanceState == null) {
            mSchedulePresenter.setSyncId(mSyncIdPreference.get(), mIsGroupSchedulePreference.get());
            RateAppDialog.newInstance(this).show();
        } else {
            showContent();
        }
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);

        initializeAnimations();

        mNightModePreference.asObservable()
                .skip(1)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(mode -> recreate());
    }

    protected abstract void findViews();

    @Override
    protected void onResume() {
        super.onResume();
        mTitlePreference.asObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .map(s -> TextUtils.split(s, " "))
                .map(strings -> Utils.getFormattedTitle(mTitleFormat, strings))
                .compose(bindToLifecycle())
                .subscribe(this::setTitle);
        registerReceiver(mReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utils.unsubscribe(mSearchViewSubscription);
        try {
            unregisterReceiver(mReceiver);
        } catch (IllegalArgumentException e) {
            Timber.e(e, "unregisterReceiver error in ScheduleActivity");
        }
    }

    @LayoutRes
    protected abstract int getLayoutRes();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSchedulePresenter.onDestroy();
        mDrawerPresenter.onDestroy();
    }

    @Override
    public void showError(Error error) {
        mProgressBar.setVisibility(View.GONE);
        switch (error) {
            case NETWORK:
                Snackbar.make(mCoordinatorLayout, getString(R.string.error_network), Snackbar.LENGTH_LONG)
                        .setAction(R.string.action_retry, v -> mSchedulePresenter.retry())
                        .show();
                break;
            case EMPTY_SCHEDULE:
                resetSyncId();
                Snackbar.make(mCoordinatorLayout, getString(R.string.error_empty_schedule),
                        Snackbar.LENGTH_LONG).show();
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
        toggleFloatingActionMenu(false);
        selectGroupOrEmployee(id, name, isGroupSchedule);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_schedule_activity, menu);
        var item = menu.findItem(R.id.action_search);
        var searchView = (SearchView) item.getActionView();
        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                mSearchViewSubscription = RxSearchView.queryTextChanges(searchView)
                        .observeOn(AndroidSchedulers.mainThread())
                        .debounce(500, TimeUnit.MILLISECONDS)
                        .subscribe(charSequence -> {
                            Intent queryIntent = new Intent(ACTION_SEARCH_QUERY);
                            queryIntent.putExtra(EXTRA_SEARCH_QUERY, charSequence);
                            mLocalBroadcastManager.sendBroadcast(queryIntent);
                        });
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Utils.unsubscribe(mSearchViewSubscription);
                var queryIntent = new Intent(ACTION_SEARCH_QUERY);
                queryIntent.putExtra(EXTRA_SEARCH_QUERY, "");
                mLocalBroadcastManager.sendBroadcast(queryIntent);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                mSchedulePresenter.retry();
                return true;
            case R.id.action_current_week:
                showCurrentWeek();
                return true;
            case R.id.action_delete:
                mSchedulePresenter.remove(mSyncIdPreference.get(), mIsGroupSchedulePreference.get());
                resetSyncId();
                return true;
            case R.id.action_filter_both:
                setFilter(item, SubgroupFilter.BOTH);
                return true;
            case R.id.action_filter_first:
                setFilter(item, SubgroupFilter.FIRST);
                return true;
            case R.id.action_filter_second:
                setFilter(item, SubgroupFilter.SECOND);
                return true;
            case R.id.action_filter_none:
                setFilter(item, SubgroupFilter.NONE);
                return true;
            case R.id.action_favorite:
                mDrawerPresenter.onCreate();
                setFavoriteState(item);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setFavoriteState(MenuItem item) {
        if (item.isChecked()) {
            Utils.cancelNotificationAlarm(this);
            item.setChecked(false).setIcon(R.drawable.ic_action_favorite_off);
            mFavoriteSyncIdPreference.set(mFavoriteSyncIdPreference.defaultValue());
            mFavoriteIsGroupSchedulePreference.set(mFavoriteIsGroupSchedulePreference.defaultValue());
            mFavoriteTitlePreference.set(mFavoriteTitlePreference.defaultValue());
        } else {
            Utils.setNotificationAlarm(this, mLocalTimePreference.get());
            item.setChecked(true).setIcon(R.drawable.ic_action_favorite_on);
            mFavoriteSyncIdPreference.set(mSyncIdPreference.get());
            mFavoriteIsGroupSchedulePreference.set(mIsGroupSchedulePreference.get());
            mFavoriteTitlePreference.set(mTitlePreference.get());
        }
    }

    private void setFilter(MenuItem item, SubgroupFilter filter) {
        item.setChecked(true);
        mSubgroupFilterPreference.set(filter);
    }

    private void resetSyncId() {
        var favoriteSyncId = mFavoriteSyncIdPreference.get();
        if (favoriteSyncId != null && favoriteSyncId.equals(mSyncIdPreference.get())) {
            mFavoriteSyncIdPreference.set(mFavoriteSyncIdPreference.defaultValue());
            mFavoriteTitlePreference.set(mFavoriteTitlePreference.defaultValue());
        }
        mSyncIdPreference.set(mSyncIdPreference.defaultValue());
        mTitlePreference.set(mTitlePreference.defaultValue());
        mItemIdPreference.set(mItemIdPreference.defaultValue());
        supportInvalidateOptionsMenu();
    }

    protected abstract void showCurrentWeek();

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.setGroupVisible(R.id.group_items, mSyncIdPreference.get() != null);
        var favoriteItem = menu.findItem(R.id.action_favorite);
        if (mSyncIdPreference.get() != null && mSyncIdPreference.get()
                .equals(mFavoriteSyncIdPreference.get())) {
            favoriteItem.setChecked(true).setIcon(R.drawable.ic_action_favorite_on);
        } else {
            favoriteItem.setChecked(false).setIcon(R.drawable.ic_action_favorite_off);
        }
        switch (mSubgroupFilterPreference.get()) {
            case BOTH:
                menu.findItem(R.id.action_filter_both).setChecked(true);
                break;
            case FIRST:
                menu.findItem(R.id.action_filter_first).setChecked(true);
                break;
            case SECOND:
                menu.findItem(R.id.action_filter_second).setChecked(true);
                break;
            case NONE:
                menu.findItem(R.id.action_filter_none).setChecked(true);
                break;
        }
        switch (Utils.getCurrentWeekNumber()) {
            case 1:
                menu.findItem(R.id.action_current_week).setIcon(R.drawable.ic_action_week_one);
                break;
            case 2:
                menu.findItem(R.id.action_current_week).setIcon(R.drawable.ic_action_week_two);
                break;
            case 3:
                menu.findItem(R.id.action_current_week).setIcon(R.drawable.ic_action_week_three);
                break;
            case 4:
                menu.findItem(R.id.action_current_week).setIcon(R.drawable.ic_action_week_four);
                break;
        }
        menu.findItem(R.id.action_refresh).setEnabled(Utils.hasNetworkConnection(this));
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void updateGroupList(Map<Integer, String> groupMap) {
        var menu = mNavigationView.getMenu();
        var groupHeader = menu.findItem(R.id.navigation_view_groups_header);
        if (groupMap.size() > 0) {
            groupHeader.setVisible(true);
            groupHeader.getSubMenu().clear();
            for (var id : groupMap.keySet()) {
                groupHeader.getSubMenu().add(R.id.navigation_view_groups, id, Menu.NONE, groupMap.get(id));
            }
            var item = menu.getItem(0);
            item.setTitle(item.getTitle());
        } else {
            groupHeader.setVisible(false);
        }
    }

    @Override
    public void updateEmployeeList(Map<Integer, String> employeeMap) {
        var menu = mNavigationView.getMenu();
        var employeeHeader = menu.findItem(R.id.navigation_view_employees_header);
        if (employeeMap.size() > 0) {
            employeeHeader.setVisible(true);
            employeeHeader.getSubMenu().clear();
            for (int id : employeeMap.keySet()) {
                employeeHeader.getSubMenu()
                        .add(R.id.navigation_view_employees, id, Menu.NONE, employeeMap.get(id));
            }
            var item = menu.getItem(0);
            item.setTitle(item.getTitle());
        } else {
            employeeHeader.setVisible(false);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        var itemId = menuItem.getItemId();
        if (mItemIdPreference.get() != menuItem.getItemId()) {
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
                var feedbackIntent =
                        new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", mFeedbackEmail, null));
                feedbackIntent.putExtra(Intent.EXTRA_SUBJECT, mFeedbackSubject);
                startActivity(Intent.createChooser(feedbackIntent, mSendFeedbackTitle));
                return true;
            }
        }
        if (mDrawerLayout != null) mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void selectGroupOrEmployee(int id, String title, boolean isGroupSchedule) {
        mItemIdPreference.set(id);
        mSharedPreferences.edit()
                .putString(PreferenceHelper.SYNC_ID, String.valueOf(id))
                .putBoolean(PreferenceHelper.IS_GROUP_SCHEDULE, isGroupSchedule)
                .apply();
        mSchedulePresenter.setSyncId(String.valueOf(id), isGroupSchedule);
        mTitlePreference.set(title);
        supportInvalidateOptionsMenu();
    }

    private void setupNavigationView() {
        getDelegate().setSupportActionBar(mToolbar);
        if (mDrawerLayout != null) {
            getDelegate().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            mToolbar.setNavigationOnClickListener(v -> mDrawerLayout.openDrawer(GravityCompat.START));
            mToolbar.setNavigationIcon(R.drawable.ic_action_navigation_menu);
        }
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    private void addStorageFragment() {
        var manager = getSupportFragmentManager();
        var fragment = (StorageFragment) manager.findFragmentByTag(TAG_STORAGE_FRAGMENT);

        if (fragment == null) {
            fragment = new StorageFragment();
            manager.beginTransaction().add(fragment, TAG_STORAGE_FRAGMENT).commit();
        }
    }

    private void toggleFloatingActionMenu(boolean enabled) {
        if (mDrawerLayout != null) {
            mDrawerLayout.setDrawerLockMode(
                    enabled ? DrawerLayout.LOCK_MODE_LOCKED_CLOSED : DrawerLayout.LOCK_MODE_UNLOCKED);
        }
        mFloatingActionMenu.setClickable(enabled);
        if (enabled) {
            mFloatingActionMenu.setOnClickListener(view -> toggleFloatingActionMenu(false));
            mFabGroup.show();
            mFabEmployee.show();
            mFamBackgroundValueAnimator.start();
            mFabValueAnimator.start();
        } else {
            mFabGroup.hide();
            mFabEmployee.hide();
            mFamBackgroundValueAnimator.reverse();
            mFabValueAnimator.reverse();
        }
    }

    private void initializeAnimations() {
        mFabValueAnimator = ValueAnimator.ofInt(0, 10000).setDuration(ANIMATION_DURATION);
        mFabValueAnimator.addUpdateListener(
                a -> mFloatingActionButton.getDrawable().setLevel((int) a.getAnimatedValue()));

        mFamBackgroundValueAnimator = ValueAnimator.ofInt(0, 255).setDuration(ANIMATION_DURATION);
        mFamBackgroundValueAnimator.addUpdateListener(
                a -> mFloatingActionMenu.getBackground().setAlpha((int) a.getAnimatedValue()));
    }
}
