package by.toggi.rxbsuir.receiver;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import org.parceler.Parcels;

import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.activity.LessonActivity;
import by.toggi.rxbsuir.activity.WeekScheduleActivity;
import by.toggi.rxbsuir.db.model.Lesson;
import by.toggi.rxbsuir.service.AppWidgetScheduleService;
import timber.log.Timber;

public class AppWidgetScheduleProvider extends AppWidgetProvider {

    public static final String ACTION_LESSON_ACTIVITY = "by.toggi.rxbsuir.action.ACTION_LESSON_ACTIVITY";
    public static final String EXTRA_LESSON = "by.toggi.rxbsuir.extra.LESSON";


    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(ACTION_LESSON_ACTIVITY)) {
            int id = intent.getIntExtra(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID
            );
            Bundle hackBundle = intent.getBundleExtra(EXTRA_LESSON);
            Lesson lesson = Parcels.unwrap(hackBundle.getParcelable(EXTRA_LESSON));
            LessonActivity.start(context, lesson);
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0, length = appWidgetIds.length; i < length; i++) {
            int id = appWidgetIds[i];

            Intent intent = new Intent(context, AppWidgetScheduleService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            Intent startActivity = new Intent(context, WeekScheduleActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, startActivity, 0);

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_schedule);
            remoteViews.setRemoteAdapter(R.id.list_view, intent);
            remoteViews.setEmptyView(R.id.list_view, R.id.empty_state);
            remoteViews.setOnClickPendingIntent(R.id.icon, pendingIntent);

            Intent lessonActivityIntent = new Intent(context, AppWidgetScheduleProvider.class);
            lessonActivityIntent.setAction(ACTION_LESSON_ACTIVITY);
            lessonActivityIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
            lessonActivityIntent.setData(Uri.parse(lessonActivityIntent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent lessonActivityPendingIntent = PendingIntent.getBroadcast(
                    context,
                    0,
                    lessonActivityIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );
            remoteViews.setPendingIntentTemplate(R.id.list_view, lessonActivityPendingIntent);

            appWidgetManager.updateAppWidget(id, remoteViews);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        for (int i = 0, length = appWidgetIds.length; i < length; i++) {
            int id = appWidgetIds[i];
            Timber.d("%s deleted: %s",
                    PreferenceHelper.getWidgetPreferencesName(id),
                    PreferenceHelper.getWidgetPreferencesFile(context, id).delete());
        }
    }
}
