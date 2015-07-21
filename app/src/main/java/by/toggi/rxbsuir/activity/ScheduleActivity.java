package by.toggi.rxbsuir.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.adapter.WeekPagerAdapter;
import by.toggi.rxbsuir.component.DaggerScheduleActivityComponent;
import by.toggi.rxbsuir.fragment.DataFragment;
import by.toggi.rxbsuir.fragment.WeekFragment;
import by.toggi.rxbsuir.module.ActivityModule;
import by.toggi.rxbsuir.module.ScheduleActivityModule;
import by.toggi.rxbsuir.mvp.presenter.SchedulePresenter;
import by.toggi.rxbsuir.mvp.view.ScheduleView;
import rx.android.view.ViewActions;
import rx.android.widget.WidgetObservable;


public class ScheduleActivity extends AppCompatActivity implements ScheduleView, SwipeRefreshLayout.OnRefreshListener {

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.tab_layout) TabLayout mTabLayout;
    @Bind(R.id.view_pager) ViewPager mViewPager;
    @Bind(R.id.progress_bar) ProgressBar mProgressBar;
    @Bind(R.id.error_text_view) TextView mErrorTextView;
    @Bind(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;

    @Inject WeekPagerAdapter mPagerAdapter;
    @Inject SchedulePresenter mPresenter;

    private List<String> mStudentGroupList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        FragmentManager manager = getSupportFragmentManager();
        DataFragment fragment = (DataFragment) manager.findFragmentByTag(WeekFragment.TAG_DATA_FRAGMENT);

        if (fragment == null) {
            fragment = new DataFragment();
            manager.beginTransaction().add(fragment, WeekFragment.TAG_DATA_FRAGMENT).commit();
        }

        initializeComponent();
        ButterKnife.bind(this);

        getDelegate().setSupportActionBar(mToolbar);

        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mTabLayout.setupWithViewPager(mViewPager);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.accent);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mPresenter.attachView(this);
        mPresenter.onCreate();
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
        TextInputLayout textInputLayout = (TextInputLayout) View.inflate(this, R.layout.dialog_add_group, null);
        AutoCompleteTextView textView = ButterKnife.findById(textInputLayout, R.id.group_number_text_view);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mStudentGroupList);
        textView.setAdapter(adapter);
        MaterialDialog dialog = new MaterialDialog.Builder(this).customView(textInputLayout, true)
                .title(R.string.title_add_group)
                .positiveText(R.string.positive_add)
                .negativeText(android.R.string.cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        mPresenter.setGroupNumber(textView.getText().toString());
                    }
                })
                .build();
        dialog.show();
        // Input validation
        WidgetObservable.text(textView).map(onTextChangeEvent -> onTextChangeEvent.text().toString())
                .map(mPresenter::isValidGroupNumber)
                .startWith(false)
                .distinctUntilChanged()
                .subscribe(ViewActions.setEnabled(dialog.getActionButton(DialogAction.POSITIVE)));
    }

    @Override
    public void showError(Throwable throwable) {
        mProgressBar.setVisibility(View.GONE);
        mErrorTextView.setVisibility(View.VISIBLE);
        mErrorTextView.setText(throwable.getLocalizedMessage());
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
        mErrorTextView.setVisibility(View.GONE);
    }

    @Override
    public void finishRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void updateStudentGroupList(List<String> studentGroupList) {
        mStudentGroupList.clear();
        mStudentGroupList.addAll(studentGroupList);
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        mPresenter.getStudentGroupSchedule();
    }

}
