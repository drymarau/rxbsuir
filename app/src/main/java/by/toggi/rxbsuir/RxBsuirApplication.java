package by.toggi.rxbsuir;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import by.toggi.rxbsuir.dagger.component.AppComponent;
import by.toggi.rxbsuir.dagger.component.DaggerAppComponent;
import by.toggi.rxbsuir.dagger.module.AppModule;
import by.toggi.rxbsuir.dagger.module.BsuirServiceModule;
import com.crashlytics.android.Crashlytics;
import com.f2prateek.rx.preferences.Preference;
import com.jakewharton.threetenabp.AndroidThreeTen;
import io.fabric.sdk.android.Fabric;
import javax.inject.Inject;
import javax.inject.Named;
import timber.log.Timber;

public class RxBsuirApplication extends Application {

  private static AppComponent mAppComponent;

  public static AppComponent getAppComponent() {
    return mAppComponent;
  }

  @Inject @Named(PreferenceHelper.NIGHT_MODE) Preference<String> mNightModePreference;

  @Override public void onCreate() {
    super.onCreate();

    AndroidThreeTen.init(this);

    if (BuildConfig.DEBUG) {
      Timber.plant(new Timber.DebugTree());
    } else {
      Fabric.with(this, new Crashlytics());
      Timber.plant(new CrashReportingTree());
    }

    mAppComponent = DaggerAppComponent.builder()
        .application(this)
        .bsuirServiceModule(new BsuirServiceModule(getString(R.string.schedule_endpoint)))
        .build();
    mAppComponent.inject(this);

    mNightModePreference.asObservable()
        .map(Integer::valueOf)
        .onErrorReturn(throwable -> AppCompatDelegate.MODE_NIGHT_NO)
        .subscribe(AppCompatDelegate::setDefaultNightMode);
  }

  private static class CrashReportingTree extends Timber.Tree {

    @Override protected void log(int priority, String tag, String message, Throwable t) {
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
