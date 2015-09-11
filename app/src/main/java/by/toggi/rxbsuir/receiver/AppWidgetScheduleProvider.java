package by.toggi.rxbsuir.receiver;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.widget.RemoteViews;

import org.parceler.Parcels;

import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.SyncIdItem;
import by.toggi.rxbsuir.activity.LessonActivity;
import by.toggi.rxbsuir.activity.WeekScheduleActivity;
import by.toggi.rxbsuir.service.AppWidgetScheduleService;
import timber.log.Timber;

public class AppWidgetScheduleProvider extends AppWidgetProvider {

    public static final String EXTRA_LESSON = "by.toggi.rxbsuir.extra.LESSON";
    public static final String EXTRA_IS_TODAY = "by.toggi.rxbsuir.extra.IS_TODAY";

    private static final String ACTION_ARROW_CLICK = "by.toggi.rxbsuir.action.ARROW_CLICK";
    private static final String ACTION_LESSON_ACTIVITY = "by.toggi.rxbsuir.action.ACTION_LESSON_ACTIVITY";
    private static final String ACTION_UPDATE_NOTE = "by.toggi.rxbsuir.action.ACTION_UPDATE_NOTE";

    public static void updateNote(Context context) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        int[] ids = manager.getAppWidgetIds(new ComponentName(context, AppWidgetScheduleProvider.class));
        Intent intent = new Intent(context, AppWidgetScheduleProvider.class);
        intent.setAction(ACTION_UPDATE_NOTE);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
        context.sendBroadcast(intent);
    }

    /**
     * Gets remote views.
     *
     * @param context the context
     * @param id      the id
     * @param isToday the is today
     * @return the remote views
     */
    @Nullable
    public static RemoteViews getRemoteViews(Context context, int id, boolean isToday) {
        SyncIdItem item = PreferenceHelper.getSyncIdItemPreference(context, id);
        if (item == null) {
            return null;
        }

        boolean isDarkTheme = PreferenceHelper.getIsDarkThemePreference(context, id);

        Intent intent = new Intent(context, AppWidgetScheduleService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
        intent.putExtra(EXTRA_IS_TODAY, isToday);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        Intent startActivity = new Intent(context, WeekScheduleActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, startActivity, 0);

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.appwidget_schedule);
        remoteViews.setInt(
                R.id.widget_background,
                "setBackgroundResource",
                isDarkTheme ? R.color.window_background_dark : R.color.window_background_light
        );
        remoteViews.setRemoteAdapter(R.id.list_view, intent);
        remoteViews.setEmptyView(R.id.list_view, R.id.empty_state);
        remoteViews.setTextColor(R.id.empty_state, isDarkTheme
                ? ContextCompat.getColor(context, android.R.color.primary_text_dark)
                : ContextCompat.getColor(context, android.R.color.primary_text_light));
        remoteViews.setTextViewText(R.id.empty_state, isToday
                ? context.getString(R.string.empty_state_today)
                : context.getString(R.string.empty_state_tomorrow));
        remoteViews.setOnClickPendingIntent(R.id.icon, pendingIntent);
        remoteViews.setImageViewResource(R.id.action_next, isToday ? R.drawable.ic_action_next : R.drawable.ic_action_previous);
        remoteViews.setTextViewText(R.id.title, context.getString(isToday ? R.string.widget_today : R.string.widget_tomorrow));
        remoteViews.setTextViewText(R.id.subtitle, item.getTitle());

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

        Intent clickIntent = new Intent(context, AppWidgetScheduleProvider.class);
        clickIntent.setAction(ACTION_ARROW_CLICK);
        clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
        clickIntent.putExtra(EXTRA_IS_TODAY, isToday);
        clickIntent.setData(Uri.parse(clickIntent.toUri(Intent.URI_INTENT_SCHEME)));
        PendingIntent clickPendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                clickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        remoteViews.setOnClickPendingIntent(R.id.action_next, clickPendingIntent);

        return remoteViews;
    }

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int id = intent.getIntExtra(
                AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID
        );
        switch (intent.getAction()) {
            case ACTION_LESSON_ACTIVITY:
                Bundle hackBundle = intent.getBundleExtra(EXTRA_LESSON);
                LessonActivity.startFromWidget(
                        context,
                        Parcels.unwrap(hackBundle.getParcelable(EXTRA_LESSON))
                );
                break;
            case ACTION_ARROW_CLICK:
                RemoteViews remoteViews = getRemoteViews(
                        context,
                        id,
                        !intent.getBooleanExtra(EXTRA_IS_TODAY, true)
                );
                if (remoteViews != null) {
                    appWidgetManager.updateAppWidget(id, remoteViews);
                }
                break;
            case ACTION_UPDATE_NOTE:
                appWidgetManager.notifyAppWidgetViewDataChanged(
                        intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS),
                        R.id.list_view
                );
                break;
        }
        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0, length = appWidgetIds.length; i < length; i++) {
            int id = appWidgetIds[i];

            RemoteViews remoteViews = getRemoteViews(context, id, true);
            if (remoteViews != null) {
                appWidgetManager.updateAppWidget(id, remoteViews);
            }
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
