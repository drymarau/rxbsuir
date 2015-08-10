package by.toggi.rxbsuir.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import javax.inject.Inject;

import butterknife.Bind;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.SubheaderItemDecoration;
import by.toggi.rxbsuir.adapter.LessonAdapter;
import by.toggi.rxbsuir.component.DaggerTermScheduleActivityComponent;
import by.toggi.rxbsuir.module.TermScheduleActivityModule;

public class TermScheduleActivity extends ScheduleActivity {

    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;

    @Inject LinearLayoutManager mLayoutManager;
    @Inject LessonAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupRecyclerView();

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

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SubheaderItemDecoration(
                LayoutInflater.from(this).inflate(R.layout.list_item_subheader, mRecyclerView, false),
                getResources().getDimensionPixelSize(R.dimen.list_subheader_height)
        ));
    }

}
