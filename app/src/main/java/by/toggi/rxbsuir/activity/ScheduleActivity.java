package by.toggi.rxbsuir.activity;

import android.animation.ValueAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.RateAppDialog;
import by.toggi.rxbsuir.Utils;
import by.toggi.rxbsuir.fragment.AddEmployeeDialogFragment;
import by.toggi.rxbsuir.fragment.AddGroupDialogFragment;
import by.toggi.rxbsuir.fragment.OnButtonClickListener;
import timber.log.Timber;

public abstract class ScheduleActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnButtonClickListener {

    private static final String TAG_ADD_GROUP_DIALOG = "add_group_dialog";
    private static final String TAG_ADD_EMPLOYEE_DIALOG = "add_employee_dialog";
    private static final long ANIMATION_DURATION = 250;

    @Inject
    SharedPreferences mSharedPreferences;

    private Toolbar mToolbar;
    private CoordinatorLayout mCoordinatorLayout;
    private FloatingActionButton mFabGroup;
    private FloatingActionButton mFabEmployee;
    private RelativeLayout mFloatingActionMenu;
    private FloatingActionButton mFloatingActionButton;
    @Nullable
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    protected int mPageMargin;

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(@NonNull Context context, @NonNull Intent intent) {
            ScheduleActivity.this.supportInvalidateOptionsMenu();
        }
    };
    private ValueAnimator mFabValueAnimator;
    private ValueAnimator mFamBackgroundValueAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutRes());

        mToolbar = findViewById(R.id.toolbar);
        mCoordinatorLayout = findViewById(R.id.coordinator_layout);
        mFabGroup = findViewById(R.id.fab_group);
        mFabEmployee = findViewById(R.id.fab_employee);
        mFloatingActionMenu = findViewById(R.id.fam);
        mFloatingActionButton = findViewById(R.id.fab);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.navigation_view);
        mPageMargin = getResources().getDimensionPixelSize(R.dimen.view_pager_page_margin);
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

        mFloatingActionMenu.getBackground().setAlpha(0);

        setupNavigationView();

        if (savedInstanceState == null) {
            RateAppDialog.newInstance(this).show();
        }
        initializeAnimations();
    }

    protected abstract void findViews();

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            unregisterReceiver(mReceiver);
        } catch (IllegalArgumentException e) {
            Timber.e(e, "unregisterReceiver error in ScheduleActivity");
        }
    }

    @LayoutRes
    protected abstract int getLayoutRes();

    @Override
    public void onPositiveButtonClicked(int id, String name, boolean isGroupSchedule) {
        toggleFloatingActionMenu(false);
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
                return true;
            case R.id.action_current_week:
                showCurrentWeek();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected abstract void showCurrentWeek();

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
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
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (mDrawerLayout != null) mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
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
