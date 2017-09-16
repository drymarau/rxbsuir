package by.toggi.rxbsuir;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;
import by.toggi.rxbsuir.dagger.component.AppComponent;
import by.toggi.rxbsuir.dagger.component.DaggerAppComponent;
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

  @Inject Timber.Tree mTree;
  @Inject @Named(PreferenceHelper.NIGHT_MODE) Preference<String> mNightModePreference;

  @Override public void onCreate() {
    super.onCreate();

    AndroidThreeTen.init(this);

    mAppComponent = DaggerAppComponent.builder()
        .application(this)
        .debug(BuildConfig.DEBUG)
        .bsuirUrl("https://students.bsuir.by/api/v1")
        .build();
    mAppComponent.inject(this);

    Timber.plant(mTree);

    if (!BuildConfig.DEBUG) {
      Fabric.with(this, new Crashlytics());
    }

    mNightModePreference.asObservable()
        .map(Integer::valueOf)
        .onErrorReturn(throwable -> AppCompatDelegate.MODE_NIGHT_NO)
        .subscribe(AppCompatDelegate::setDefaultNightMode);
  }
}
