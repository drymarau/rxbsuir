package by.toggi.rxbsuir.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import org.threeten.bp.LocalTime;

import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.Utils;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        scheduleAlarms(context);
    }

    private void scheduleAlarms(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String favoriteSyncId = preferences.getString(PreferenceHelper.FAVORITE_SYNC_ID, null);
        if (favoriteSyncId != null) {
            String localTimeString = preferences.getString(PreferenceHelper.NOTIFICATION_TIME, "07:00");
            Utils.setAlarm(context, LocalTime.parse(localTimeString));
        }
    }
}
