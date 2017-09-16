package by.toggi.rxbsuir.dagger.component;

import android.app.Application;
import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.activity.AppWidgetConfigActivity;
import by.toggi.rxbsuir.activity.LessonActivity;
import by.toggi.rxbsuir.activity.ScheduleActivity;
import by.toggi.rxbsuir.activity.SettingsActivity;
import by.toggi.rxbsuir.activity.WeekScheduleActivity;
import by.toggi.rxbsuir.dagger.PerApp;
import by.toggi.rxbsuir.dagger.module.AppModule;
import by.toggi.rxbsuir.dagger.module.BsuirServiceModule;
import by.toggi.rxbsuir.dagger.module.DbModule;
import by.toggi.rxbsuir.dagger.module.PaperworkModule;
import by.toggi.rxbsuir.dagger.module.PreferencesModule;
import by.toggi.rxbsuir.dagger.module.TimberTreeModule;
import by.toggi.rxbsuir.fragment.AddEmployeeDialogFragment;
import by.toggi.rxbsuir.fragment.AddGroupDialogFragment;
import by.toggi.rxbsuir.fragment.AppWidgetConfigFragment;
import by.toggi.rxbsuir.fragment.SettingsFragment;
import by.toggi.rxbsuir.night_mode.NightModePreferenceModule;
import by.toggi.rxbsuir.service.AppWidgetScheduleService;
import by.toggi.rxbsuir.service.LessonReminderService;
import by.toggi.rxbsuir.service.ReplaceSyncIdService;
import dagger.BindsInstance;
import dagger.Component;
import javax.inject.Named;

@PerApp @Component(modules = {
    AppModule.class, TimberTreeModule.class, BsuirServiceModule.class, DbModule.class,
    PreferencesModule.class, PaperworkModule.class, NightModePreferenceModule.class
}) public interface AppComponent {

  LessonListFragmentComponent.Builder lessonListFragmentComponentBuilder();

  void inject(RxBsuirApplication application);

  void inject(AddGroupDialogFragment addGroupDialogFragment);

  void inject(AddEmployeeDialogFragment addEmployeeDialogFragment);

  void inject(SettingsActivity settingsActivity);

  void inject(ScheduleActivity scheduleActivity);

  void inject(LessonReminderService lessonReminderService);

  void inject(SettingsFragment settingsFragment);

  void inject(WeekScheduleActivity weekScheduleActivity);

  void inject(ReplaceSyncIdService replaceSyncIdService);

  void inject(LessonActivity lessonActivity);

  void inject(AppWidgetScheduleService appWidgetScheduleService);

  void inject(AppWidgetConfigFragment appWidgetConfigFragment);

  void inject(AppWidgetConfigActivity appWidgetConfigActivity);

  @Component.Builder interface Builder {

    @BindsInstance Builder application(Application application);

    @BindsInstance Builder debug(@Named("debug") boolean debug);

    @BindsInstance Builder bsuirUrl(@Named("bsuir") String url);

    AppComponent build();
  }
}
