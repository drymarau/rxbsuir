package by.toggi.rxbsuir.module;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import javax.inject.Named;
import javax.inject.Singleton;

import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.activity.ScheduleActivity;
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
    @Named(value = ScheduleActivity.KEY_IS_GROUP_SCHEDULE)
    boolean provideIsGroupSchedule(SharedPreferences preferences) {
        return preferences.getBoolean(ScheduleActivity.KEY_IS_GROUP_SCHEDULE, true);
    }

    @Provides
    @Named(value = ScheduleActivity.KEY_IS_DARK_THEME)
    boolean provideIsDarkTheme(SharedPreferences preferences) {
        return preferences.getBoolean(ScheduleActivity.KEY_IS_DARK_THEME, false);
    }

    @Nullable
    @Provides
    @Named(value = ScheduleActivity.KEY_SYNC_ID)
    String provideSyncId(SharedPreferences preferences) {
        return preferences.getString(ScheduleActivity.KEY_SYNC_ID, null);
    }

}
