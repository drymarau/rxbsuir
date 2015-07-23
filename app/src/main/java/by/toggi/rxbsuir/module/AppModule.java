package by.toggi.rxbsuir.module;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

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

}
