package by.toggi.rxbsuir.component;

import android.content.SharedPreferences;

import com.f2prateek.rx.preferences.Preference;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import javax.inject.Named;
import javax.inject.Singleton;

import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.SubgroupFilter;
import by.toggi.rxbsuir.activity.SettingsActivity;
import by.toggi.rxbsuir.fragment.AddEmployeeDialogFragment;
import by.toggi.rxbsuir.fragment.AddGroupDialogFragment;
import by.toggi.rxbsuir.module.AppModule;
import by.toggi.rxbsuir.module.BsuirServiceModule;
import by.toggi.rxbsuir.module.DbModule;
import by.toggi.rxbsuir.module.PreferencesModule;
import by.toggi.rxbsuir.rest.BsuirService;
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

    BsuirService bsuirService();

    StorIOSQLite storIOSQLite();

    SharedPreferences sharedPreferences();

    @Named(PreferenceHelper.SYNC_ID) Preference<String> rxSyncId();

    @Named(PreferenceHelper.IS_GROUP_SCHEDULE) Preference<Boolean> rxIsGroupSchedule();

    @Named(PreferenceHelper.TITLE) Preference<String> rxTitle();

    Preference<Integer> rxItemId();

    Preference<SubgroupFilter> rxSubgroupFilter();

    @Named(PreferenceHelper.IS_DARK_THEME) boolean isDarkTheme();

    void inject(AddGroupDialogFragment addGroupDialogFragment);

    void inject(AddEmployeeDialogFragment addEmployeeDialogFragment);

    void inject(SettingsActivity settingsActivity);

}
