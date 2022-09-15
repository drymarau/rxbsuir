package by.toggi.rxbsuir.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;

import java.time.LocalTime;

import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.Utils;

public class BootReceiver extends BroadcastReceiver {

    @Override public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        scheduleAlarms(context);
    }

    private void scheduleAlarms(Context context) {
        var preferences = PreferenceManager.getDefaultSharedPreferences(context);
        var favoriteSyncId = preferences.getString(PreferenceHelper.FAVORITE_SYNC_ID, null);
        if (favoriteSyncId != null) {
            var localTimeString = preferences.getString(PreferenceHelper.NOTIFICATION_TIME, "07:00");
            Utils.setNotificationAlarm(context, LocalTime.parse(localTimeString));
        }
    }
}
