package by.toggi.rxbsuir;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.support.v7.app.AppCompatDelegate;
import by.toggi.rxbsuir.activity.AppWidgetConfigActivity;
import by.toggi.rxbsuir.activity.LessonActivity;
import by.toggi.rxbsuir.activity.SettingsActivity;
import by.toggi.rxbsuir.activity.WeekScheduleActivity;
import by.toggi.rxbsuir.dagger.PerApp;
import by.toggi.rxbsuir.dagger.module.AppModule;
import by.toggi.rxbsuir.dagger.module.BsuirServiceModule;
import by.toggi.rxbsuir.dagger.module.DbModule;
import by.toggi.rxbsuir.dagger.module.PaperworkModule;
import by.toggi.rxbsuir.dagger.module.PreferencesModule;
import by.toggi.rxbsuir.dagger.module.TimberTreeModule;
import by.toggi.rxbsuir.night_mode.NightModePreferenceModule;
import by.toggi.rxbsuir.service.AppWidgetScheduleService;
import by.toggi.rxbsuir.service.LessonReminderService;
import by.toggi.rxbsuir.service.ReplaceSyncIdService;
import com.crashlytics.android.Crashlytics;
import com.f2prateek.rx.preferences.Preference;
import com.jakewharton.threetenabp.AndroidThreeTen;
import dagger.BindsInstance;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasServiceInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import io.fabric.sdk.android.Fabric;
import javax.inject.Inject;
import javax.inject.Named;
import timber.log.Timber;

public class App extends Application implements HasActivityInjector, HasServiceInjector {

  @Inject DispatchingAndroidInjector<Activity> mDispatchingActivityInjector;
  @Inject DispatchingAndroidInjector<Service> mDispatchingServiceInjector;
  @Inject Timber.Tree mTree;
  @Inject @Named(PreferenceHelper.NIGHT_MODE) Preference<String> mNightModePreference;

  @Override public void onCreate() {
    super.onCreate();

    AndroidThreeTen.init(this);

    DaggerApp_Component.builder()
        .application(this)
        .debug(BuildConfig.DEBUG)
        .bsuirUrl("https://students.bsuir.by/api/v1")
        .build()
        .inject(this);

    Timber.plant(mTree);

    if (!BuildConfig.DEBUG) {
      Fabric.with(this, new Crashlytics());
    }

    mNightModePreference.asObservable()
        .map(Integer::valueOf)
        .onErrorReturn(throwable -> AppCompatDelegate.MODE_NIGHT_NO)
        .subscribe(AppCompatDelegate::setDefaultNightMode);

    LessonReminderService.enqueueWork(this);
  }

  @Override public AndroidInjector<Activity> activityInjector() {
    return mDispatchingActivityInjector;
  }

  @Override public AndroidInjector<Service> serviceInjector() {
    return mDispatchingServiceInjector;
  }

  @PerApp @dagger.Component(modules = {
      AndroidSupportInjectionModule.class, AppModule.class, TimberTreeModule.class,
      BsuirServiceModule.class, DbModule.class, PreferencesModule.class, PaperworkModule.class,
      NightModePreferenceModule.class, SettingsActivity.Module.class, LessonActivity.Module.class,
      AppWidgetConfigActivity.Module.class, LessonReminderService.Module.class,
      ReplaceSyncIdService.Module.class, AppWidgetScheduleService.Module.class,
      WeekScheduleActivity.Module.class
  }) public interface Component {

    void inject(App application);

    @dagger.Component.Builder interface Builder {

      @BindsInstance Builder application(Application application);

      @BindsInstance Builder debug(@Named("debug") boolean debug);

      @BindsInstance Builder bsuirUrl(@Named("bsuir") String url);

      Component build();
    }
  }
}
