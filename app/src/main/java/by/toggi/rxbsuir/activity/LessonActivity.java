package by.toggi.rxbsuir.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.Bind;
import butterknife.ButterKnife;
import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.adapter.DetailItemAdapter;
import by.toggi.rxbsuir.db.model.Lesson;
import by.toggi.rxbsuir.mvp.presenter.LessonDetailPresenter;
import by.toggi.rxbsuir.mvp.view.LessonDetailView;

public class LessonActivity extends AppCompatActivity implements LessonDetailView {

    private static final String EXTRA_LESSON = "by.toggi.rxbsuir.extra.lesson";

    @Bind(R.id.toolbar) Toolbar mToolbar;
    @Bind(R.id.collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbarLayout;
    @Bind(R.id.recycler_view) RecyclerView mRecyclerView;

    @Inject @Named(PreferenceHelper.IS_DARK_THEME) boolean mIsDarkTheme;
    @Inject LessonDetailPresenter mPresenter;

    private DetailItemAdapter mAdapter;

    public static void start(Context context, Lesson lesson) {
        Intent intent = new Intent(context, LessonActivity.class);
        intent.putExtra(EXTRA_LESSON, Parcels.wrap(lesson));
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((RxBsuirApplication) getApplication()).getAppComponent().inject(this);

        setTheme(mIsDarkTheme ? R.style.AppTheme_Dark : R.style.AppTheme_Light);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        ButterKnife.bind(this);

        setupToolbar();


        Lesson lesson = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_LESSON));

        mCollapsingToolbarLayout.setTitle(lesson.getSubjectWithSubgroup());

        mAdapter = new DetailItemAdapter(new ArrayList<>());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mPresenter.attachView(this);
        mPresenter.setLesson(lesson);
    }

    @Override
    public void showLessonDetail(List<DetailItem> detailItemList) {
        mAdapter.setDetailItemList(detailItemList);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        getDelegate().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    public static class DetailItem {

        private Type mType;
        private String mText;
        private String mSummary;
        private boolean mIsIconVisible;

        private DetailItem() {
        }

        public static DetailItem newInstance(Type type, String text, String summary, boolean isIconVisible) {
            DetailItem detailItem = new DetailItem();
            detailItem.mType = type;
            detailItem.mText = text;
            detailItem.mSummary = summary;
            detailItem.mIsIconVisible = isIconVisible;
            return detailItem;
        }

        public static DetailItem newInstance(Type type, String text, boolean isIconVisible) {
            DetailItem detailItem = new DetailItem();
            detailItem.mType = type;
            detailItem.mText = text;
            detailItem.mIsIconVisible = isIconVisible;
            return detailItem;
        }

        public Type getType() {
            return mType;
        }

        public String getText() {
            return mText;
        }

        public String getSummary() {
            return mSummary;
        }

        public boolean isIconVisible() {
            return mIsIconVisible;
        }

        public enum Type {
            TIME, EMPLOYEE, GROUP, AUDITORY, WEEKDAY, NOTE
        }

    }

}
