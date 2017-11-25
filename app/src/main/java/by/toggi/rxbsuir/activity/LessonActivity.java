package by.toggi.rxbsuir.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.BindView;
import butterknife.ButterKnife;
import by.toggi.rxbsuir.DividerItemDecoration;
import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.adapter.DetailItemAdapter;
import by.toggi.rxbsuir.dagger.PerActivity;
import by.toggi.rxbsuir.db.model.Lesson;
import by.toggi.rxbsuir.mvp.presenter.LessonDetailPresenter;
import by.toggi.rxbsuir.mvp.view.LessonDetailView;
import by.toggi.rxbsuir.receiver.AppWidgetScheduleProvider;
import com.afollestad.materialdialogs.MaterialDialog;
import com.f2prateek.rx.preferences.Preference;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import dagger.android.AndroidInjection;
import dagger.android.ContributesAndroidInjector;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import org.parceler.Parcels;

public class LessonActivity extends RxAppCompatActivity implements LessonDetailView {

  private static final String EXTRA_LESSON = "by.toggi.rxbsuir.extra.lesson";

  @BindView(R.id.toolbar) Toolbar mToolbar;
  @BindView(R.id.recycler_view) RecyclerView mRecyclerView;

  @Inject LessonDetailPresenter mPresenter;
  @Inject @Named(PreferenceHelper.NIGHT_MODE) Preference<String> mNightModePreference;

  private DetailItemAdapter mAdapter;

  public static void start(Context context, Lesson lesson) {
    Intent intent = new Intent(context, LessonActivity.class);
    intent.putExtra(EXTRA_LESSON, Parcels.wrap(lesson));
    context.startActivity(intent);
  }

  public static void startFromWidget(Context context, Lesson lesson) {
    Intent intent = new Intent(context, LessonActivity.class);
    intent.putExtra(EXTRA_LESSON, Parcels.wrap(lesson));
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    AndroidInjection.inject(this);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_lesson);

    ButterKnife.bind(this);

    setupToolbar();

    Lesson lesson = Parcels.unwrap(getIntent().getParcelableExtra(EXTRA_LESSON));

    getDelegate().getSupportActionBar().setTitle(lesson.getSubjectWithSubgroup());
    getDelegate().getSupportActionBar().setSubtitle(lesson.getLessonType());

    mAdapter = new DetailItemAdapter(this, new ArrayList<>());
    mRecyclerView.setAdapter(mAdapter);
    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    mRecyclerView.addItemDecoration(DividerItemDecoration.vertical(this, R.drawable.padded_divider));

    mPresenter.attachView(this);
    mPresenter.setLesson(lesson);

    mNightModePreference.asObservable()
        .skip(1)
        .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
        .subscribe(mode -> recreate());
  }

  @Override public void showLessonDetail(List<DetailItem> detailItemList) {
    mAdapter.setDetailItemList(detailItemList);
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    mPresenter.onDestroy();
  }

  private void setupToolbar() {
    setSupportActionBar(mToolbar);
    getDelegate().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    mToolbar.setNavigationOnClickListener(v -> onBackPressed());
  }

  @Override public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_lesson_activity, menu);
    return true;
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.action_edit:
        showEditDialog();
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  private void showEditDialog() {
    new MaterialDialog.Builder(this).title(R.string.title_add_note)
        .input(getString(R.string.hint_note), mPresenter.getLessonNote(), true,
            (materialDialog, charSequence) -> {
              mPresenter.setLessonNote(charSequence.toString());
              AppWidgetScheduleProvider.updateNote(this);
            })
        .neutralText(R.string.neutral_clear)
        .callback(new MaterialDialog.ButtonCallback() {
          @Override public void onNeutral(MaterialDialog dialog) {
            super.onNeutral(dialog);
            mPresenter.setLessonNote(null);
            AppWidgetScheduleProvider.updateNote(LessonActivity.this);
            dialog.dismiss();
          }
        })
        .positiveText(R.string.positive_add)
        .negativeText(android.R.string.cancel)
        .autoDismiss(true)
        .show();
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

  @dagger.Module public interface Module {

    @PerActivity @ContributesAndroidInjector LessonActivity contribute();
  }
}
