package by.toggi.rxbsuir;

import android.app.Application;

import com.jakewharton.threetenabp.AndroidThreeTen;

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

        AndroidThreeTen.init(this);

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } else {
            Timber.plant(new CrashReportingTree());
        }

        if (mAppComponent == null) {
            mAppComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(this))
//                    .bsuirServiceModule(new BsuirServiceModule(getString(R.string.schedule_endpoint)))
                    .bsuirServiceModule(new BsuirServiceModule("http://web.archive.org/web/20141106111025/http://www.bsuir.by/schedule/rest/"))
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
