package by.toggi.rxbsuir.module;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;

import javax.inject.Named;
import javax.inject.Singleton;

import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.RxBsuirApplication;
import dagger.Module;
import dagger.Provides;

@Module
public class PreferencesModule {

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences(RxBsuirApplication application) {
        return PreferenceManager.getDefaultSharedPreferences(application);
    }

    @Provides
    @Named(PreferenceHelper.IS_DARK_THEME)
    boolean provideIsDarkTheme(SharedPreferences preferences) {
        return preferences.getBoolean(PreferenceHelper.IS_DARK_THEME, false);
    }

    @Provides
    @Singleton
    RxSharedPreferences provideRxSharedPreferences(SharedPreferences preferences) {
        return RxSharedPreferences.create(preferences);
    }

    @Provides
    @Singleton
    @Named(PreferenceHelper.SYNC_ID)
    Preference<String> provideRxSyncId(RxSharedPreferences preferences) {
        return preferences.getString(PreferenceHelper.SYNC_ID, null);
    }

    @Provides
    @Singleton
    @Named(PreferenceHelper.IS_GROUP_SCHEDULE)
    Preference<Boolean> provideRxIsGroupSchedule(RxSharedPreferences preferences) {
        return preferences.getBoolean(PreferenceHelper.IS_GROUP_SCHEDULE, true);
    }

    @Provides
    @Singleton
    @Named(PreferenceHelper.TITLE)
    Preference<String> provideRxTitle(RxSharedPreferences preferences, RxBsuirApplication application) {
        return preferences.getString(PreferenceHelper.TITLE, application.getString(R.string.app_name));
    }

    @Provides
    @Singleton
    Preference<Integer> provideRxItemId(RxSharedPreferences preferences) {
        return preferences.getInteger(PreferenceHelper.ITEM_ID, -1);
    }

}
