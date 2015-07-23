package by.toggi.rxbsuir.module;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

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

    @Nullable
    @Provides
    String provideGroupNumber(SharedPreferences preferences) {
        return preferences.getString(ScheduleActivity.KEY_GROUP_NUMBER, null);
    }

}
