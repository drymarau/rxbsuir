package by.toggi.rxbsuir.receiver;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.RemoteViews;
import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.SyncIdItem;
import by.toggi.rxbsuir.Utils;
import by.toggi.rxbsuir.activity.LessonActivity;
import by.toggi.rxbsuir.activity.WeekScheduleActivity;
import by.toggi.rxbsuir.dagger.PerBroadcastReceiver;
import by.toggi.rxbsuir.mvp.presenter.LessonListPresenter.SubgroupFilter;
import by.toggi.rxbsuir.service.AppWidgetScheduleService;
import com.f2prateek.rx.preferences.Preference;
import dagger.android.AndroidInjection;
import dagger.android.ContributesAndroidInjector;
import javax.inject.Inject;
import org.parceler.Parcels;
import timber.log.Timber;

public class AppWidgetScheduleProvider extends AppWidgetProvider {

  public static final String EXTRA_LESSON = "by.toggi.rxbsuir.extra.LESSON";
  public static final String EXTRA_IS_TODAY = "by.toggi.rxbsuir.extra.IS_TODAY";

  private static final String ACTION_ARROW_CLICK = "by.toggi.rxbsuir.action.ARROW_CLICK";
  private static final String ACTION_LESSON_ACTIVITY =
      "by.toggi.rxbsuir.action.ACTION_LESSON_ACTIVITY";
  private static final String ACTION_UPDATE_NOTE = "by.toggi.rxbsuir.action.ACTION_UPDATE_NOTE";

  @Inject Preference.Adapter<SyncIdItem> mAdapter;

  /**
   * Updates note in all available widgets.
   *
   * @param context the context
   */
  public static void updateNote(Context context) {
    AppWidgetManager manager = AppWidgetManager.getInstance(context);
    int[] ids =
        manager.getAppWidgetIds(new ComponentName(context, AppWidgetScheduleProvider.class));
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
   * @param id the id
   * @return the remote views
   */
  @Nullable public static RemoteViews getRemoteViews(Context context, int id,
      Preference.Adapter<SyncIdItem> adapter) {
    SyncIdItem item = PreferenceHelper.getSyncIdItemPreference(context, id, adapter);
    if (item == null) {
      return null;
    }

    boolean isNightModeEnabled = PreferenceHelper.isNightModeEnabled(context, id);
    boolean isToday = PreferenceHelper.getIsTodayPreference(context, id);
    boolean isCollapsed = PreferenceHelper.getIsWidgetCollapsedPreference(context, id);
    SubgroupFilter subgroupFilter = PreferenceHelper.getSubgroupFilterPreference(context, id);

    RemoteViews remoteViews =
        new RemoteViews(context.getPackageName(), R.layout.appwidget_schedule);

    remoteViews.setInt(R.id.icon, "setVisibility", isCollapsed ? View.GONE : View.VISIBLE);

    setupRemoteViews(context, id, isToday, isNightModeEnabled, remoteViews);

    setupSubtitle(item, subgroupFilter, remoteViews);

    setupOpenApp(context, remoteViews, isCollapsed ? R.id.title_subtitle : R.id.icon);

    setupItemPendingIntentTemplate(context, id, remoteViews);

    setupArrow(context, id, isToday, remoteViews);

    return remoteViews;
  }

  private static void setupSubtitle(SyncIdItem item, SubgroupFilter subgroupFilter,
      RemoteViews remoteViews) {
    String title = item.getTitle();
    switch (subgroupFilter) {
      case BOTH:
        remoteViews.setTextViewText(R.id.subtitle, title);
        break;
      case FIRST:
        remoteViews.setTextViewText(R.id.subtitle, title + " (1)");
        break;
      case SECOND:
        remoteViews.setTextViewText(R.id.subtitle, title + " (2)");
        break;
      case NONE:
        remoteViews.setTextViewText(R.id.subtitle, title + " (0)");
        break;
    }
  }

  private static void setupRemoteViews(Context context, int id, boolean isToday,
      boolean isDarkTheme, RemoteViews remoteViews) {
    Intent intent = new Intent(context, AppWidgetScheduleService.class);
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
    intent.putExtra(EXTRA_IS_TODAY, isToday);
    intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

    remoteViews.setRemoteAdapter(R.id.list_view, intent);
    remoteViews.setEmptyView(R.id.list_view, R.id.empty_state);

    remoteViews.setTextColor(R.id.empty_state,
        isDarkTheme ? ContextCompat.getColor(context, android.R.color.primary_text_dark)
            : ContextCompat.getColor(context, android.R.color.primary_text_light));
    remoteViews.setTextViewText(R.id.empty_state,
        isToday ? context.getString(R.string.empty_state_today)
            : context.getString(R.string.empty_state_tomorrow));
    remoteViews.setInt(R.id.widget_background, "setBackgroundResource", R.color.window_background);
  }

  private static void setupOpenApp(Context context, RemoteViews remoteViews, @IdRes int viewId) {
    Intent startActivity = new Intent(context, WeekScheduleActivity.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, startActivity, 0);
    remoteViews.setOnClickPendingIntent(viewId, pendingIntent);
  }

  private static void setupItemPendingIntentTemplate(Context context, int id,
      RemoteViews remoteViews) {
    Intent lessonActivityIntent = new Intent(context, AppWidgetScheduleProvider.class);
    lessonActivityIntent.setAction(ACTION_LESSON_ACTIVITY);
    lessonActivityIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
    lessonActivityIntent.setData(Uri.parse(lessonActivityIntent.toUri(Intent.URI_INTENT_SCHEME)));
    PendingIntent lessonActivityPendingIntent =
        PendingIntent.getBroadcast(context, 0, lessonActivityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT);
    remoteViews.setPendingIntentTemplate(R.id.list_view, lessonActivityPendingIntent);
  }

  private static void setupArrow(Context context, int id, boolean isToday,
      RemoteViews remoteViews) {
    Intent clickIntent = new Intent(context, AppWidgetScheduleProvider.class);
    clickIntent.setAction(ACTION_ARROW_CLICK);
    clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
    clickIntent.putExtra(EXTRA_IS_TODAY, isToday);
    clickIntent.setData(Uri.parse(clickIntent.toUri(Intent.URI_INTENT_SCHEME)));
    PendingIntent clickPendingIntent =
        PendingIntent.getBroadcast(context, 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);

    remoteViews.setOnClickPendingIntent(R.id.action_next, clickPendingIntent);
    remoteViews.setImageViewResource(R.id.action_next,
        isToday ? R.drawable.ic_action_next : R.drawable.ic_action_previous);
    remoteViews.setTextViewText(R.id.title,
        context.getString(isToday ? R.string.widget_today : R.string.widget_tomorrow));
  }

  @Override @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager,
      int appWidgetId, Bundle newOptions) {
    PreferenceHelper.setIsWidgetCollapsedPreference(context, appWidgetId,
        newOptions.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH) <= 220);
    RemoteViews remoteViews = getRemoteViews(context, appWidgetId, mAdapter);
    if (remoteViews != null) {
      appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
      appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.list_view);
    }
  }

  @Override public void onReceive(@NonNull Context context, @NonNull Intent intent) {
    AndroidInjection.inject(this, context);
    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
    int id = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
        AppWidgetManager.INVALID_APPWIDGET_ID);
    switch (intent.getAction()) {
      case ACTION_LESSON_ACTIVITY:
        Bundle hackBundle = intent.getBundleExtra(EXTRA_LESSON);
        LessonActivity.startFromWidget(context,
            Parcels.unwrap(hackBundle.getParcelable(EXTRA_LESSON)));
        break;
      case ACTION_ARROW_CLICK:
        PreferenceHelper.setIsTodayPreference(context, id,
            !intent.getBooleanExtra(EXTRA_IS_TODAY, true));
        RemoteViews remoteViews = getRemoteViews(context, id, mAdapter);
        if (remoteViews != null) {
          appWidgetManager.updateAppWidget(id, remoteViews);
        }
        break;
      case ACTION_UPDATE_NOTE:
        appWidgetManager.notifyAppWidgetViewDataChanged(
            intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS), R.id.list_view);
        break;
    }
    super.onReceive(context, intent);
  }

  @Override
  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    for (int id : appWidgetIds) {
      RemoteViews remoteViews = getRemoteViews(context, id, mAdapter);
      if (remoteViews != null) {
        appWidgetManager.updateAppWidget(id, remoteViews);
      }
    }
  }

  @Override public void onDeleted(Context context, int[] appWidgetIds) {
    super.onDeleted(context, appWidgetIds);
    for (int id : appWidgetIds) {
      Timber.d("%s deleted: %s", PreferenceHelper.getWidgetPreferencesName(id),
          PreferenceHelper.getWidgetPreferencesFile(context, id).delete());
    }
  }

  @Override public void onEnabled(Context context) {
    super.onEnabled(context);
    Utils.setWidgetUpdateAlarm(context);
  }

  @Override public void onDisabled(Context context) {
    super.onDisabled(context);
    Utils.cancelWidgetUpdateAlarm(context);
  }

  @dagger.Module public interface Module {

    @PerBroadcastReceiver @ContributesAndroidInjector AppWidgetScheduleProvider contribute();
  }
}
