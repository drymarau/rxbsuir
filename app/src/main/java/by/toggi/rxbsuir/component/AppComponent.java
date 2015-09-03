package by.toggi.rxbsuir.component;

import android.content.SharedPreferences;

import com.f2prateek.rx.preferences.Preference;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import javax.inject.Named;
import javax.inject.Singleton;

import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.SubgroupFilter;
import by.toggi.rxbsuir.activity.ScheduleActivity;
import by.toggi.rxbsuir.activity.SettingsActivity;
import by.toggi.rxbsuir.activity.WeekScheduleActivity;
import by.toggi.rxbsuir.fragment.AddEmployeeDialogFragment;
import by.toggi.rxbsuir.fragment.AddGroupDialogFragment;
import by.toggi.rxbsuir.fragment.SettingsFragment;
import by.toggi.rxbsuir.module.AppModule;
import by.toggi.rxbsuir.module.BsuirServiceModule;
import by.toggi.rxbsuir.module.DbModule;
import by.toggi.rxbsuir.module.PreferencesModule;
import by.toggi.rxbsuir.service.LessonReminderService;
import by.toggi.rxbsuir.service.ReplaceSyncIdService;
import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        BsuirServiceModule.class,
        DbModule.class,
        PreferencesModule.class
})
public interface AppComponent {

    RxBsuirApplication app();

    StorIOSQLite storIOSQLite();

    SharedPreferences preferences();

    @Named(PreferenceHelper.SYNC_ID) Preference<String> rxSyncId();

    @Named(PreferenceHelper.IS_GROUP_SCHEDULE) Preference<Boolean> rxIsGroupSchedule();

    Preference<SubgroupFilter> rxSubgroupFilter();

    SubgroupFilter subgroupFilter();

    void inject(AddGroupDialogFragment addGroupDialogFragment);

    void inject(AddEmployeeDialogFragment addEmployeeDialogFragment);

    void inject(SettingsActivity settingsActivity);

    void inject(ScheduleActivity scheduleActivity);

    void inject(LessonReminderService lessonReminderService);

    void inject(SettingsFragment settingsFragment);

    void inject(WeekScheduleActivity weekScheduleActivity);

    void inject(ReplaceSyncIdService replaceSyncIdService);

}
