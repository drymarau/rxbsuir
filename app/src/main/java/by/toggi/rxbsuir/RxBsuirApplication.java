package by.toggi.rxbsuir;

import android.app.Application;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.jakewharton.threetenabp.AndroidThreeTen;

import by.toggi.rxbsuir.dagger.component.AppComponent;
import by.toggi.rxbsuir.dagger.component.DaggerAppComponent;
import by.toggi.rxbsuir.dagger.module.AppModule;
import by.toggi.rxbsuir.dagger.module.BsuirServiceModule;
import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class RxBsuirApplication extends Application {

    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        AndroidThreeTen.init(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Fabric.with(this, new Crashlytics());
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
            if (priority == Log.VERBOSE || priority == Log.DEBUG) {
                return;
            }

            Crashlytics.log(priority, tag, message);
            if (t != null) {
                Crashlytics.logException(t);
            }
        }
    }

}
