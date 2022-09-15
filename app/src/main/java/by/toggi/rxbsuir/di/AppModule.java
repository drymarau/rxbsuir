package by.toggi.rxbsuir.di;

import javax.inject.Singleton;

import by.toggi.rxbsuir.mvp.presenter.NavigationDrawerPresenter;
import by.toggi.rxbsuir.mvp.presenter.SchedulePresenter;
import by.toggi.rxbsuir.rest.BsuirService;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {

    @Provides
    @Singleton
    SchedulePresenter provideSchedulePresenter(BsuirService service) {
        return new SchedulePresenter(service);
    }

    @Provides
    @Singleton
    NavigationDrawerPresenter provideNavigationDrawerPresenter() {
        return new NavigationDrawerPresenter();
    }
}
