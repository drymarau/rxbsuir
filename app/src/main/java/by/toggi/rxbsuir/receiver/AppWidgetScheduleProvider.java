package by.toggi.rxbsuir.receiver;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.activity.WeekScheduleActivity;

public class AppWidgetScheduleProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int i = 0, length = appWidgetIds.length; i < length; i++) {
            int id = appWidgetIds[i];

            Intent intent = new Intent(context, WeekScheduleActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget_schedule);
            views.setOnClickPendingIntent(R.id.appwidget_circle, pendingIntent);

            appWidgetManager.updateAppWidget(id, views);
        }
    }
}
