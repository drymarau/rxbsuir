package by.toggi.rxbsuir.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;

import com.f2prateek.rx.preferences.Preference;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.Utils;
import by.toggi.rxbsuir.activity.WeekScheduleActivity;
import by.toggi.rxbsuir.db.model.Lesson;

import static by.toggi.rxbsuir.db.RxBsuirContract.LessonEntry;

public class LessonReminderService extends IntentService {

    @Inject StorIOSQLite mStorIOSQLite;
    @Inject @Named(PreferenceHelper.FAVORITE_SYNC_ID) Preference<String> mFavoriteSyncIdPreference;
    @Inject @Named(PreferenceHelper.FAVORITE_IS_GROUP_SCHEDULE) Preference<Boolean> mFavoriteIsGroupSchedule;

    public LessonReminderService() {
        super(LessonReminderService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((RxBsuirApplication) getApplication()).getAppComponent().inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // Get lesson list for current day
        if (mFavoriteSyncIdPreference.get() != null) {
            DayOfWeek dayOfWeek = LocalDate.now().getDayOfWeek();
            int weekNumber = Utils.getCurrentWeekNumber();
            Query query = Query.builder()
                    .table(LessonEntry.TABLE_NAME)
                    .where(LessonEntry.getSyncIdTypeDayOfWeekAndWeekNumberQuery())
                    .whereArgs(
                            mFavoriteSyncIdPreference.get(),
                            mFavoriteIsGroupSchedule.get() ? 1 : 0,
                            dayOfWeek.toString(),
                            "%" + weekNumber + "%").build();
            List<Lesson> lessonList = mStorIOSQLite.get()
                    .listOfObjects(Lesson.class)
                    .withQuery(query)
                    .prepare().executeAsBlocking();
            if (!lessonList.isEmpty()) {
                showNotification(lessonList);
            }
        }
    }

    private void showNotification(List<Lesson> lessonList) {
        Context context = getApplicationContext();
        Intent resultIntent = new Intent(context, WeekScheduleActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);

        stackBuilder.addParentStack(WeekScheduleActivity.class);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        for (int i = 0, size = lessonList.size(); i < size; i++) {
            inboxStyle.addLine(lessonList.get(i).getPrettyLesson());
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notification)
                .setStyle(inboxStyle)
                .setAutoCancel(true)
                .setContentTitle("RxBSUIR")
                .setContentIntent(resultPendingIntent);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(100, builder.build());
    }
}
