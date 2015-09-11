package by.toggi.rxbsuir.service;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import org.parceler.Parcels;
import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.SyncIdItem;
import by.toggi.rxbsuir.Utils;
import by.toggi.rxbsuir.db.model.Lesson;
import by.toggi.rxbsuir.mvp.presenter.LessonListPresenter.SubgroupFilter;
import by.toggi.rxbsuir.receiver.AppWidgetScheduleProvider;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static by.toggi.rxbsuir.db.RxBsuirContract.LessonEntry;

public class AppWidgetScheduleService extends RemoteViewsService {

    @Inject StorIOSQLite mStorIOSQLite;

    @Override
    public void onCreate() {
        super.onCreate();
        ((RxBsuirApplication) getApplication()).getAppComponent().inject(this);
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new AppWidgetScheduleFactory(this, intent, mStorIOSQLite);
    }

    public static class AppWidgetScheduleFactory implements RemoteViewsFactory {

        private final Context mContext;
        private final StorIOSQLite mStorIOSQLite;
        private final boolean mIsToday;
        private final SyncIdItem mSyncIdItem;
        private final boolean mAreCirclesColored;
        private final boolean mIsDarkTheme;
        private final SubgroupFilter mSubgroupFilter;
        private Subscription mSubscription;
        private List<Lesson> mLessonList = new ArrayList<>();

        public AppWidgetScheduleFactory(Context context, Intent intent, StorIOSQLite storIOSQLite) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            mContext = context;
            mIsToday = intent.getBooleanExtra(AppWidgetScheduleProvider.EXTRA_IS_TODAY, true);
            mStorIOSQLite = storIOSQLite;
            mSyncIdItem = PreferenceHelper.getSyncIdItemPreference(context, appWidgetId);
            mAreCirclesColored = PreferenceHelper.getAreCirclesColoredPreference(context, appWidgetId);
            mIsDarkTheme = PreferenceHelper.getIsDarkThemePreference(context, appWidgetId);
            mSubgroupFilter = PreferenceHelper.getSubgroupFilterPreference(context, appWidgetId);
        }

        @Override
        public void onCreate() {
            LocalDate date = mIsToday ? LocalDate.now() : LocalDate.now().plusDays(1);
            Utils.unsubscribe(mSubscription);
            mSubscription = mStorIOSQLite.get()
                    .listOfObjects(Lesson.class)
                    .withQuery(Query.builder()
                            .table(LessonEntry.TABLE_NAME)
                            .where(LessonEntry.Query.builder(mSyncIdItem.getSyncId(), mSyncIdItem.isGroupSchedule())
                                    .weekNumber(Utils.getCurrentWeekNumber())
                                    .weekDay(date.getDayOfWeek())
                                    .subgroupFilter(mSubgroupFilter)
                                    .build().toString())
                            .build())
                    .prepare()
                    .createObservable()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(lessonList -> {
                        mLessonList = lessonList;
                        AppWidgetScheduleProvider.updateNote(mContext);
                    });
        }

        @Override
        public void onDataSetChanged() {
        }

        @Override
        public void onDestroy() {
            Utils.unsubscribe(mSubscription);
        }

        @Override
        public int getCount() {
            return mLessonList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            Lesson lesson = mLessonList.get(position);
            RemoteViews views = new RemoteViews(mContext.getPackageName(), mIsDarkTheme
                    ? R.layout.appwidget_item_dark
                    : R.layout.appwidget_item_light);
            if (mAreCirclesColored) {
                switch (lesson.getLessonType().toLowerCase()) {
                    case "лр":
                        views.setInt(R.id.lesson_type, "setBackgroundResource", R.drawable.circle_widget_lab);
                        break;
                    case "пз":
                        views.setInt(R.id.lesson_type, "setBackgroundResource", R.drawable.circle_widget_practice);
                        break;
                    case "лк":
                        views.setInt(R.id.lesson_type, "setBackgroundResource", R.drawable.circle_widget_lecture);
                        break;
                }
            }
            views.setTextViewText(R.id.lesson_type, lesson.getLessonType());
            views.setTextViewText(R.id.lesson_subject_subgroup, lesson.getSubjectWithSubgroup());
            if (lesson.getNote() != null && !lesson.getNote().isEmpty()) {
                views.setInt(R.id.lesson_note, "setVisibility", View.VISIBLE);
            } else {
                views.setInt(R.id.lesson_note, "setVisibility", View.GONE);
            }
            views.setTextViewText(R.id.lesson_class, lesson.getPrettyAuditoryList());
            views.setTextViewText(R.id.lesson_time_start, lesson.getPrettyLessonTimeStart());
            views.setTextViewText(R.id.lesson_time_end, lesson.getPrettyLessonTimeEnd());

            Intent lessonActivityIntent = new Intent();
            Bundle hackBundle = new Bundle();
            hackBundle.putParcelable(AppWidgetScheduleProvider.EXTRA_LESSON, Parcels.wrap(lesson));
            lessonActivityIntent.putExtra(AppWidgetScheduleProvider.EXTRA_LESSON, hackBundle);
            views.setOnClickFillInIntent(R.id.item_lesson, lessonActivityIntent);
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return new RemoteViews(mContext.getPackageName(), R.layout.empty_view);
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }

}
