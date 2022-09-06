package by.toggi.rxbsuir;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;
import by.toggi.rxbsuir.activity.AppWidgetConfigActivity;
import by.toggi.rxbsuir.activity.LessonActivity;
import by.toggi.rxbsuir.activity.SettingsActivity;
import by.toggi.rxbsuir.activity.WeekScheduleActivity;
import by.toggi.rxbsuir.dagger.PerApp;
import by.toggi.rxbsuir.dagger.module.AppModule;
import by.toggi.rxbsuir.dagger.module.BsuirServiceModule;
import by.toggi.rxbsuir.dagger.module.DbModule;
import by.toggi.rxbsuir.dagger.module.GsonModule;
import by.toggi.rxbsuir.dagger.module.OkHttpClientModule;
import by.toggi.rxbsuir.dagger.module.PreferencesModule;
import by.toggi.rxbsuir.dagger.module.TimberTreeModule;
import by.toggi.rxbsuir.night_mode.NightModePreferenceModule;
import by.toggi.rxbsuir.receiver.AppWidgetScheduleProvider;
import by.toggi.rxbsuir.service.AppWidgetScheduleService;
import by.toggi.rxbsuir.service.LessonReminderService;
import by.toggi.rxbsuir.service.ReplaceSyncIdService;
import com.f2prateek.rx.preferences.Preference;
import com.jakewharton.threetenabp.AndroidThreeTen;
import dagger.BindsInstance;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import javax.inject.Inject;
import javax.inject.Named;
import timber.log.Timber;

public class App extends Application implements HasAndroidInjector {

  @Inject DispatchingAndroidInjector<Object> mAndroidInjector;
  @Inject Timber.Tree mTree;
  @Inject @Named(PreferenceHelper.NIGHT_MODE) Preference<String> mNightModePreference;

  @Override public void onCreate() {
    super.onCreate();

    AndroidThreeTen.init(this);

    DaggerApp_Component.builder()
        .application(this)
        .debug(BuildConfig.DEBUG)
        .bsuirUrl("https://journal.bsuir.by/")
        .build()
        .inject(this);

    Timber.plant(mTree);

    mNightModePreference.asObservable()
        .map(Integer::valueOf)
        .onErrorReturn(throwable -> AppCompatDelegate.MODE_NIGHT_NO)
        .subscribe(AppCompatDelegate::setDefaultNightMode);
  }

  @Override public AndroidInjector<Object> androidInjector() {
    return mAndroidInjector;
  }

  @PerApp @dagger.Component(modules = {
      AndroidSupportInjectionModule.class, AppModule.class, TimberTreeModule.class,
      BsuirServiceModule.class, DbModule.class, PreferencesModule.class,
      NightModePreferenceModule.class, GsonModule.class, OkHttpClientModule.class,
      SettingsActivity.Module.class, LessonActivity.Module.class,
      AppWidgetConfigActivity.Module.class, LessonReminderService.Module.class,
      ReplaceSyncIdService.Module.class, AppWidgetScheduleService.Module.class,
      WeekScheduleActivity.Module.class, AppWidgetScheduleProvider.Module.class
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
