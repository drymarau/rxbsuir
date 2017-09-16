package by.toggi.rxbsuir.dagger.module;

import android.content.Context;
import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.dagger.PerApp;
import by.toggi.rxbsuir.mvp.presenter.NavigationDrawerPresenter;
import by.toggi.rxbsuir.mvp.presenter.SchedulePresenter;
import by.toggi.rxbsuir.rest.BsuirService;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import dagger.Module;
import dagger.Provides;

@Module public class AppModule {

  private final RxBsuirApplication mApplication;

  public AppModule(RxBsuirApplication application) {
    mApplication = application;
  }

  @Provides @PerApp RxBsuirApplication provideAppContext() {
    return mApplication;
  }

  @Provides @PerApp Context provideContext() {
    return mApplication;
  }

  @Provides @PerApp SchedulePresenter provideSchedulePresenter(BsuirService service,
      StorIOSQLite storIOSQLite) {
    return new SchedulePresenter(service, storIOSQLite);
  }

  @Provides @PerApp NavigationDrawerPresenter provideNavigationDrawerPresenter(
      StorIOSQLite storIOSQLite) {
    return new NavigationDrawerPresenter(storIOSQLite);
  }
}
