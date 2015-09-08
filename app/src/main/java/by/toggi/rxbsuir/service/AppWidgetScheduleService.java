package by.toggi.rxbsuir.service;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import org.threeten.bp.LocalDate;

import java.util.List;

import javax.inject.Inject;

import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.Utils;
import by.toggi.rxbsuir.db.model.Lesson;
import timber.log.Timber;

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
        private final int mAppWidgetId;
        private final StorIOSQLite mStorIOSQLite;
        private List<Lesson> mLessonList;

        public AppWidgetScheduleFactory(Context context, Intent intent, StorIOSQLite storIOSQLite) {
            Timber.d(storIOSQLite.toString());
            mContext = context;
            mAppWidgetId = intent.getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID
            );
            mStorIOSQLite = storIOSQLite;
        }

        @Override
        public void onCreate() {
            mLessonList = mStorIOSQLite.get()
                    .listOfObjects(Lesson.class)
                    .withQuery(Query.builder()
                            .table(LessonEntry.TABLE_NAME)
                            .where(LessonEntry.Query.builder("20084", true)
                                    .weekNumber(Utils.getCurrentWeekNumber())
                                    .weekDay(LocalDate.now().getDayOfWeek())
                                    .build().toString())
                            .build())
                    .prepare().executeAsBlocking();
        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return mLessonList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(mContext.getPackageName(), android.R.layout.simple_list_item_1);
            views.setTextViewText(android.R.id.text1, mLessonList.get(position).getPrettyLesson());
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
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
