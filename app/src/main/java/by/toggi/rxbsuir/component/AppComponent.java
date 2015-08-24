package by.toggi.rxbsuir.component;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import javax.inject.Named;
import javax.inject.Singleton;

import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.activity.SettingsActivity;
import by.toggi.rxbsuir.fragment.AddEmployeeDialogFragment;
import by.toggi.rxbsuir.fragment.AddGroupDialogFragment;
import by.toggi.rxbsuir.module.AppModule;
import by.toggi.rxbsuir.module.BsuirServiceModule;
import by.toggi.rxbsuir.module.DbModule;
import by.toggi.rxbsuir.rest.BsuirService;
import dagger.Component;

@Singleton
@Component(modules = {
        AppModule.class,
        BsuirServiceModule.class,
        DbModule.class
})
public interface AppComponent {

    RxBsuirApplication app();

    BsuirService bsuirService();

    StorIOSQLite storIOSQLite();

    SharedPreferences sharedPreferences();

    RxSharedPreferences rxSharedPreferences();

    Preference<String> rxSyncId();

    Preference<Boolean> rxIsGroupSchedule();

    @Named(PreferenceHelper.IS_GROUP_SCHEDULE) boolean isGroupSchedule();

    @Named(PreferenceHelper.IS_DARK_THEME) boolean isDarkTheme();

    @Nullable @Named(PreferenceHelper.SYNC_ID) String syncId();

    void inject(AddGroupDialogFragment addGroupDialogFragment);

    void inject(AddEmployeeDialogFragment addEmployeeDialogFragment);

    void inject(SettingsActivity settingsActivity);

}
