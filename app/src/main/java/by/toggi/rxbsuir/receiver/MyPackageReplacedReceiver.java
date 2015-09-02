package by.toggi.rxbsuir.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import by.toggi.rxbsuir.BuildConfig;
import by.toggi.rxbsuir.service.ReplaceSyncIdService;

public class MyPackageReplacedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (BuildConfig.VERSION_CODE) {
            // Surprise API change
            case 12200:
                context.startService(new Intent(context, ReplaceSyncIdService.class));
                break;
        }
    }

}
