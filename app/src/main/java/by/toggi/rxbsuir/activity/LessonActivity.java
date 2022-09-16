package by.toggi.rxbsuir.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.f2prateek.rx.preferences.Preference;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import by.toggi.rxbsuir.DetailItemDecoration;
import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.Utils;
import by.toggi.rxbsuir.adapter.DetailItemAdapter;
import by.toggi.rxbsuir.model.Lesson;
import by.toggi.rxbsuir.mvp.presenter.LessonDetailPresenter;
import by.toggi.rxbsuir.mvp.view.LessonDetailView;
import dagger.hilt.android.AndroidEntryPoint;
import rx.Subscription;

@AndroidEntryPoint
public class LessonActivity extends AppCompatActivity implements LessonDetailView {

    private static final String EXTRA_LESSON = "by.toggi.rxbsuir.extra.lesson";

    @Inject
    LessonDetailPresenter mPresenter;
    @Inject
    @Named(PreferenceHelper.NIGHT_MODE)
    Preference<String> mNightModePreference;

    private Toolbar mToolbar;
    private DetailItemAdapter mAdapter;
    private Subscription mSubscription;

    public static void start(Context context, Lesson lesson) {
        Intent intent = new Intent(context, LessonActivity.class);
        intent.putExtra(EXTRA_LESSON, lesson);
        context.startActivity(intent);
    }

    public static void startFromWidget(Context context, Lesson lesson) {
        Intent intent = new Intent(context, LessonActivity.class);
        intent.putExtra(EXTRA_LESSON, lesson);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson);

        mToolbar = findViewById(R.id.toolbar);
        RecyclerView mRecyclerView = findViewById(R.id.recycler_view);

        setupToolbar();

        Lesson lesson = getIntent().getParcelableExtra(EXTRA_LESSON);

        getDelegate().getSupportActionBar().setTitle(lesson.getSubjectWithSubgroup());
        getDelegate().getSupportActionBar().setSubtitle(lesson.getLessonType());

        mAdapter = new DetailItemAdapter(this, new ArrayList<>());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DetailItemDecoration(this));

        mPresenter.attachView(this);
        mPresenter.setLesson(lesson);

        mSubscription = mNightModePreference.asObservable()
                .skip(1)
                .subscribe(mode -> recreate());
    }

    @Override
    public void showLessonDetail(List<DetailItem> detailItemList) {
        mAdapter.setDetailItemList(detailItemList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.unsubscribe(mSubscription);
        mPresenter.onDestroy();
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        getDelegate().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lesson_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public static class DetailItem {

        private Type mType;
        private String mText;
        private String mSummary;
        private boolean mIsIconVisible;

        private DetailItem() {
        }

        public static DetailItem newInstance(Type type, String text, String summary,
                                             boolean isIconVisible) {
            var detailItem = new DetailItem();
            detailItem.mType = type;
            detailItem.mText = text;
            detailItem.mSummary = summary;
            detailItem.mIsIconVisible = isIconVisible;
            return detailItem;
        }

        public static DetailItem newInstance(Type type, String text, boolean isIconVisible) {
            var detailItem = new DetailItem();
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
