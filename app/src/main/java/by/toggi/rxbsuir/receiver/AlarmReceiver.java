package by.toggi.rxbsuir.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import by.toggi.rxbsuir.service.LessonReminderService;

public class AlarmReceiver extends BroadcastReceiver {

  @Override public void onReceive(@NonNull Context context, @NonNull Intent intent) {
    LessonReminderService.enqueueWork(context);
  }
}
