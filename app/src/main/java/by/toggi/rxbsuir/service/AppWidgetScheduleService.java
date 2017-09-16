package by.toggi.rxbsuir.service;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.SyncIdItem;
import by.toggi.rxbsuir.Utils;
import by.toggi.rxbsuir.dagger.PerService;
import by.toggi.rxbsuir.db.model.Lesson;
import by.toggi.rxbsuir.mvp.presenter.LessonListPresenter.SubgroupFilter;
import by.toggi.rxbsuir.receiver.AppWidgetScheduleProvider;
import com.f2prateek.rx.preferences.Preference;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;
import dagger.android.AndroidInjection;
import dagger.android.ContributesAndroidInjector;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.parceler.Parcels;
import org.threeten.bp.LocalDate;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static by.toggi.rxbsuir.db.RxBsuirContract.LessonEntry;

public class AppWidgetScheduleService extends RemoteViewsService {

  @Inject StorIOSQLite mStorIOSQLite;
  @Inject Preference.Adapter<SyncIdItem> mAdapter;

  @Override public void onCreate() {
    AndroidInjection.inject(this);
    super.onCreate();
  }

  @Override public RemoteViewsFactory onGetViewFactory(Intent intent) {
    return new AppWidgetScheduleFactory(this, intent, mStorIOSQLite, mAdapter);
  }

  public static class AppWidgetScheduleFactory implements RemoteViewsFactory {

    private final Context mContext;
    private final StorIOSQLite mStorIOSQLite;
    private final boolean mIsToday;
    private final SyncIdItem mSyncIdItem;
    private final boolean mAreCirclesColored;
    private final boolean mIsDarkTheme;
    private final SubgroupFilter mSubgroupFilter;
    private final int mAppWidgetId;
    private Subscription mSubscription;
    private List<Lesson> mLessonList = new ArrayList<>();
    private boolean mIsCollapsed;

    public AppWidgetScheduleFactory(Context context, Intent intent, StorIOSQLite storIOSQLite,
        Preference.Adapter<SyncIdItem> adapter) {
      mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
          AppWidgetManager.INVALID_APPWIDGET_ID);
      mContext = context;
      mIsToday = intent.getBooleanExtra(AppWidgetScheduleProvider.EXTRA_IS_TODAY, true);
      mStorIOSQLite = storIOSQLite;
      mSyncIdItem = PreferenceHelper.getSyncIdItemPreference(context, mAppWidgetId, adapter);
      mAreCirclesColored = PreferenceHelper.getAreCirclesColoredPreference(context, mAppWidgetId);
      mIsDarkTheme = PreferenceHelper.isNightModeEnabled(context, mAppWidgetId);
      mSubgroupFilter = PreferenceHelper.getSubgroupFilterPreference(context, mAppWidgetId);
      mIsCollapsed = PreferenceHelper.getIsWidgetCollapsedPreference(mContext, mAppWidgetId);
    }

    @Override public void onCreate() {
      LocalDate date = mIsToday ? LocalDate.now() : LocalDate.now().plusDays(1);
      Utils.unsubscribe(mSubscription);
      mSubscription = mStorIOSQLite.get()
          .listOfObjects(Lesson.class)
          .withQuery(Query.builder()
              .table(LessonEntry.TABLE_NAME)
              .where(
                  LessonEntry.Query.builder(mSyncIdItem.getSyncId(), mSyncIdItem.isGroupSchedule())
                      .weekNumber(Utils.getWeekNumber(date))
                      .weekDay(date.getDayOfWeek())
                      .subgroupFilter(mSubgroupFilter)
                      .build()
                      .toString())
              .build())
          .prepare()
          .createObservable()
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(lessonList -> {
            mLessonList = lessonList;
            AppWidgetScheduleProvider.updateNote(mContext);
          });
    }

    @Override public void onDataSetChanged() {
      mIsCollapsed = PreferenceHelper.getIsWidgetCollapsedPreference(mContext, mAppWidgetId);
    }

    @Override public void onDestroy() {
      Utils.unsubscribe(mSubscription);
    }

    @Override public int getCount() {
      return mLessonList.size();
    }

    @Override public RemoteViews getViewAt(int position) {
      Lesson lesson = mLessonList.get(position);
      RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(),
          mIsDarkTheme ? R.layout.appwidget_item_dark : R.layout.appwidget_item_light);

      setupLessonType(remoteViews, lesson);

      remoteViews.setTextViewText(R.id.lesson_type, lesson.getLessonType());
      remoteViews.setTextViewText(R.id.lesson_subject_subgroup, lesson.getSubjectWithSubgroup());

      setupLessonNote(lesson, remoteViews);

      setupLessonAuditoryList(lesson, remoteViews);

      setupLessonTime(lesson, remoteViews);

      setupOnLessonClick(lesson, remoteViews);

      return remoteViews;
    }

    private void setupOnLessonClick(Lesson lesson, RemoteViews remoteViews) {
      Intent lessonActivityIntent = new Intent();
      Bundle hackBundle = new Bundle();
      hackBundle.putParcelable(AppWidgetScheduleProvider.EXTRA_LESSON, Parcels.wrap(lesson));
      lessonActivityIntent.putExtra(AppWidgetScheduleProvider.EXTRA_LESSON, hackBundle);
      remoteViews.setOnClickFillInIntent(R.id.item_lesson, lessonActivityIntent);
    }

    private void setupLessonTime(Lesson lesson, RemoteViews remoteViews) {
      remoteViews.setTextViewText(R.id.lesson_time_start, lesson.getPrettyLessonTimeStart());
      remoteViews.setTextViewText(R.id.lesson_time_end, lesson.getPrettyLessonTimeEnd());
    }

    private void setupLessonAuditoryList(Lesson lesson, RemoteViews remoteViews) {
      if (mIsCollapsed) {
        remoteViews.setTextViewText(R.id.lesson_class,
            String.format("(%s) %s", lesson.getLessonType(), lesson.getPrettyAuditoryList()));
      } else {
        remoteViews.setTextViewText(R.id.lesson_class, lesson.getPrettyAuditoryList());
      }
    }

    private void setupLessonNote(Lesson lesson, RemoteViews remoteViews) {
      if (lesson.getNote() != null && !lesson.getNote().isEmpty()) {
        remoteViews.setInt(R.id.lesson_note, "setVisibility", View.VISIBLE);
      } else {
        remoteViews.setInt(R.id.lesson_note, "setVisibility", View.GONE);
      }
    }

    @Override public RemoteViews getLoadingView() {
      return new RemoteViews(mContext.getPackageName(), R.layout.empty_view);
    }

    @Override public int getViewTypeCount() {
      return 1;
    }

    @Override public long getItemId(int i) {
      return 0;
    }

    @Override public boolean hasStableIds() {
      return false;
    }

    private void setupLessonType(RemoteViews remoteViews, Lesson lesson) {
      if (mIsCollapsed) {
        remoteViews.setInt(R.id.lesson_type, "setVisibility", View.GONE);
      } else {
        remoteViews.setInt(R.id.lesson_type, "setVisibility", View.VISIBLE);
        if (mAreCirclesColored) {
          switch (lesson.getLessonType().toLowerCase()) {
            case "лр":
              remoteViews.setInt(R.id.lesson_type, "setBackgroundResource", R.drawable.circle_lab);
              break;
            case "пз":
              remoteViews.setInt(R.id.lesson_type, "setBackgroundResource",
                  R.drawable.circle_practice);
              break;
            case "лк":
              remoteViews.setInt(R.id.lesson_type, "setBackgroundResource",
                  R.drawable.circle_lecture);
              break;
          }
        } else {
          remoteViews.setInt(R.id.lesson_type, "setBackgroundResource", R.drawable.circle);
        }
      }
    }
  }

  @dagger.Module public interface Module {

    @PerService @ContributesAndroidInjector AppWidgetScheduleService contribute();
  }
}
