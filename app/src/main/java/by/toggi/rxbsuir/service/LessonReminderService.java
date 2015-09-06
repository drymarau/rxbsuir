package by.toggi.rxbsuir.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.TextUtils;

import com.f2prateek.rx.preferences.Preference;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.queries.Query;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.FormatStyle;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import by.toggi.rxbsuir.IntentUtils;
import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.Utils;
import by.toggi.rxbsuir.activity.WeekScheduleActivity;
import by.toggi.rxbsuir.db.model.Lesson;
import by.toggi.rxbsuir.mvp.presenter.LessonListPresenter.SubgroupFilter;

import static by.toggi.rxbsuir.db.RxBsuirContract.LessonEntry;

public class LessonReminderService extends IntentService {

    @Inject StorIOSQLite mStorIOSQLite;
    @Inject @Named(PreferenceHelper.FAVORITE_SYNC_ID) Preference<String> mFavoriteSyncIdPreference;
    @Inject @Named(PreferenceHelper.FAVORITE_IS_GROUP_SCHEDULE) Preference<Boolean> mFavoriteIsGroupSchedule;
    @Inject @Named(PreferenceHelper.FAVORITE_TITLE) Preference<String> mFavoriteTitlePreference;
    @Inject @Named(PreferenceHelper.NOTIFICATION_SOUND_ENABLED) Preference<Boolean> mNotificationSoundEnabledPreference;
    @Inject Preference<SubgroupFilter> mSubgroupFilterPreference;

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
            Query query = Query.builder()
                    .table(LessonEntry.TABLE_NAME)
                    .where(LessonEntry.Query.builder(mFavoriteSyncIdPreference.get(), mFavoriteIsGroupSchedule.get())
                            .weekDay(dayOfWeek)
                            .subgroupFilter(mSubgroupFilterPreference.get())
                            .weekNumber(Utils.getCurrentWeekNumber())
                            .build()
                            .toString())
                    .build();
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

        String contentTitle = mFavoriteTitlePreference.asObservable()
                .map(s -> TextUtils.split(s, " "))
                .map(strings -> Utils.getFormattedTitle("%s %.1s.%.1s", strings))
                .toBlocking()
                .first();

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(contentTitle);
        inboxStyle.setSummaryText(LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)));
        for (int i = 0, size = lessonList.size(); i < size; i++) {
            inboxStyle.addLine(lessonList.get(i).getPrettyLesson());
        }

        PendingIntent sharePendingIntent = PendingIntent.getActivity(
                context,
                0,
                Intent.createChooser(IntentUtils.getDayScheduleShareIntent(
                        lessonList,
                        mFavoriteTitlePreference.get(),
                        LocalDate.now()
                ), getString(R.string.action_share_intent)),
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notification)
                .setStyle(inboxStyle)
                .setAutoCancel(true)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(contentTitle)
                .addAction(R.drawable.ic_action_share, getString(R.string.action_share), sharePendingIntent)
                .setDefaults(mNotificationSoundEnabledPreference.get()
                        ? NotificationCompat.DEFAULT_SOUND | NotificationCompat.DEFAULT_LIGHTS
                        : NotificationCompat.DEFAULT_LIGHTS)
                .setContentIntent(resultPendingIntent);
        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
        managerCompat.notify(100, builder.build());
    }
}
