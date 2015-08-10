package by.toggi.rxbsuir.activity;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import butterknife.Bind;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.component.DaggerTermScheduleActivityComponent;
import by.toggi.rxbsuir.module.TermScheduleActivityModule;

public class TermScheduleActivity extends ScheduleActivity {

    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_term_schedule;
    }

    @Override
    protected void initializeComponent() {
        DaggerTermScheduleActivityComponent.builder()
                .termScheduleActivityModule(new TermScheduleActivityModule(this))
                .appComponent(((RxBsuirApplication) getApplication()).getAppComponent())
                .build().inject(this);
    }

    @Override
    protected void showToday() {

    }
}
