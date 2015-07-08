package by.toggi.rxbsuir;

import android.app.Application;

import by.toggi.rxbsuir.component.AppComponent;
import by.toggi.rxbsuir.component.DaggerAppComponent;
import by.toggi.rxbsuir.module.AppModule;
import by.toggi.rxbsuir.module.BsuirServiceModule;
import timber.log.Timber;

public class RxBsuirApplication extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }

        if (mAppComponent == null) {
            mAppComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(this))
                    .bsuirServiceModule(new BsuirServiceModule(getString(R.string.schedule_endpoint)))
                    .build();
        }
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    private static class CrashReportingTree extends Timber.Tree {

        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
            // TODO Add crashlytics logging
            return;
        }
    }

}
