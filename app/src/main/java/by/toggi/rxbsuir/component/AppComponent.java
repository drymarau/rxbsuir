package by.toggi.rxbsuir.component;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import javax.inject.Named;
import javax.inject.Singleton;

import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.activity.ScheduleActivity;
import by.toggi.rxbsuir.activity.SettingsActivity;
import by.toggi.rxbsuir.fragment.AddEmployeeDialogFragment;
import by.toggi.rxbsuir.fragment.AddGroupDialogFragment;
import by.toggi.rxbsuir.module.AcademicCalendarServiceModule;
import by.toggi.rxbsuir.module.AppModule;
import by.toggi.rxbsuir.module.BsuirServiceModule;
import by.toggi.rxbsuir.module.DbModule;
import by.toggi.rxbsuir.rest.AcademicCalendarService;
import by.toggi.rxbsuir.rest.BsuirService;
import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        BsuirServiceModule.class,
        AcademicCalendarServiceModule.class,
        DbModule.class
})
public interface AppComponent {

    RxBsuirApplication app();

    BsuirService bsuirService();

    AcademicCalendarService academicCalendarService();

    StorIOSQLite storIOSQLite();

    SharedPreferences sharedPreferences();

    @Named(ScheduleActivity.KEY_IS_GROUP_SCHEDULE) boolean isGroupSchedule();

    @Named(ScheduleActivity.KEY_IS_DARK_THEME) boolean isDarkTheme();

    @Named(ScheduleActivity.KEY_IS_WEEK_VIEW) boolean isWeekView();

    @Nullable @Named(ScheduleActivity.KEY_SYNC_ID) String syncId();

    void inject(AddGroupDialogFragment addGroupDialogFragment);

    void inject(AddEmployeeDialogFragment addEmployeeDialogFragment);

    void inject(SettingsActivity settingsActivity);

}
