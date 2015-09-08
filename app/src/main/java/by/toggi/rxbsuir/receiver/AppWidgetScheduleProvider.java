package by.toggi.rxbsuir.receiver;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.activity.WeekScheduleActivity;
import by.toggi.rxbsuir.service.AppWidgetScheduleService;

public class AppWidgetScheduleProvider extends AppWidgetProvider {

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

            appWidgetManager.updateAppWidget(id, remoteViews);
        }
    }
}
