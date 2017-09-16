package by.toggi.rxbsuir.dagger.module;

import android.app.Application;
import android.content.Context;
import by.toggi.rxbsuir.dagger.PerApp;
import by.toggi.rxbsuir.mvp.presenter.NavigationDrawerPresenter;
import by.toggi.rxbsuir.mvp.presenter.SchedulePresenter;
import by.toggi.rxbsuir.rest.BsuirService;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import dagger.Module;
import dagger.Provides;

@Module public class AppModule {

  @Provides @PerApp Context provideContext(Application application) {
    return application;
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
