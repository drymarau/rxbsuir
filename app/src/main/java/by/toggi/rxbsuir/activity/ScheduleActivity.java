package by.toggi.rxbsuir.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

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
import icepick.Icepick;
import icepick.State;


public class ScheduleActivity extends AppCompatActivity implements ScheduleView, AddDialogFragment.OnButtonClickListener {

    public static final String KEY_GROUP_NUMBER = "selected_group_number";
    private static final String TAG_ADD_DIALOG = "add_dialog";

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.tab_layout) TabLayout mTabLayout;
    @Bind(R.id.view_pager) ViewPager mViewPager;
    @Bind(R.id.progress_bar) ProgressBar mProgressBar;
    @Bind(R.id.error_text_view) TextView mErrorTextView;

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

        addStorageFragment();

        getDelegate().setSupportActionBar(mToolbar);

        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mTabLayout.setupWithViewPager(mViewPager);

        mPresenter.attachView(this);
        mPresenter.onCreate();

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
        AddDialogFragment dialog = AddDialogFragment.newInstance();
        dialog.show(getSupportFragmentManager(), TAG_ADD_DIALOG);
    }

    @Override
    public void showError(Throwable throwable) {
        mProgressBar.setVisibility(View.GONE);
        mErrorTextView.setVisibility(View.VISIBLE);
        mErrorTextView.setText(throwable.getLocalizedMessage());
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void finishRefresh() {
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
}
