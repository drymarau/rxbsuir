package by.toggi.rxbsuir.module;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;

import javax.inject.Named;
import javax.inject.Singleton;

import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.RxBsuirApplication;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final RxBsuirApplication mApplication;

    public AppModule(RxBsuirApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    RxBsuirApplication provideAppContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    SharedPreferences provideSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(mApplication);
    }

    @Provides
    @Named(PreferenceHelper.KEY_IS_GROUP_SCHEDULE)
    boolean provideIsGroupSchedule(SharedPreferences preferences) {
        return preferences.getBoolean(PreferenceHelper.KEY_IS_GROUP_SCHEDULE, true);
    }

    @Provides
    @Named(PreferenceHelper.KEY_IS_DARK_THEME)
    boolean provideIsDarkTheme(SharedPreferences preferences) {
        return preferences.getBoolean(PreferenceHelper.KEY_IS_DARK_THEME, false);
    }

    @Nullable
    @Provides
    @Named(PreferenceHelper.KEY_SYNC_ID)
    String provideSyncId(SharedPreferences preferences) {
        return preferences.getString(PreferenceHelper.KEY_SYNC_ID, null);
    }

    @Provides
    @Singleton
    RxSharedPreferences provideRxSharedPreferences(SharedPreferences preferences) {
        return RxSharedPreferences.create(preferences);
    }

    @Provides
    @Singleton
    Preference<String> provideRxSyncId(RxSharedPreferences preferences) {
        return preferences.getString(PreferenceHelper.KEY_SYNC_ID, null);
    }

    @Provides
    Preference<Boolean> provideRxIsGroupSchedule(RxSharedPreferences preferences) {
        return preferences.getBoolean(PreferenceHelper.KEY_IS_GROUP_SCHEDULE, true);
    }

}
