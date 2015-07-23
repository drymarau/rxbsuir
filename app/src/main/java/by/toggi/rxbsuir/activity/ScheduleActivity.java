package by.toggi.rxbsuir.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.adapter.WeekPagerAdapter;
import by.toggi.rxbsuir.component.DaggerScheduleActivityComponent;
import by.toggi.rxbsuir.fragment.AddDialogFragment;
import by.toggi.rxbsuir.fragment.StorageFragment;
import by.toggi.rxbsuir.fragment.WeekFragment;
import by.toggi.rxbsuir.module.ActivityModule;
import by.toggi.rxbsuir.module.ScheduleActivityModule;
import by.toggi.rxbsuir.mvp.presenter.SchedulePresenter;
import by.toggi.rxbsuir.mvp.view.ScheduleView;


public class ScheduleActivity extends AppCompatActivity implements ScheduleView, AddDialogFragment.OnButtonClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG_ADD_DIALOG = "add_dialog";

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.tab_layout) TabLayout mTabLayout;
    @Bind(R.id.view_pager) ViewPager mViewPager;
    @Bind(R.id.progress_bar) ProgressBar mProgressBar;
    @Bind(R.id.error_text_view) TextView mErrorTextView;
    @Bind(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;

    @Inject WeekPagerAdapter mPagerAdapter;
    @Inject SchedulePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        initializeComponent();
        ButterKnife.bind(this);

        addStorageFragment();

        getDelegate().setSupportActionBar(mToolbar);

        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mTabLayout.setupWithViewPager(mViewPager);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.accent);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setEnabled(false);

        mPresenter.attachView(this);
        mPresenter.onCreate();
    }

    private void addStorageFragment() {
        FragmentManager manager = getSupportFragmentManager();
        StorageFragment fragment = (StorageFragment) manager.findFragmentByTag(WeekFragment.TAG_STORAGE_FRAGMENT);

        if (fragment == null) {
            fragment = new StorageFragment();
            manager.beginTransaction().add(fragment, WeekFragment.TAG_STORAGE_FRAGMENT).commit();
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
        AddDialogFragment dialog = AddDialogFragment.newInstance();
        dialog.show(getSupportFragmentManager(), TAG_ADD_DIALOG);
    }

    @Override
    public void showError(Throwable throwable) {
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void finishRefresh() {
    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void onPositiveButtonClicked(String groupNumber) {
        Toast.makeText(this, String.format("Group number: %s", groupNumber), Toast.LENGTH_SHORT).show();
    }
}
