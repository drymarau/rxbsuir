package by.toggi.rxbsuir.dagger.module;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import javax.inject.Singleton;

import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.mvp.presenter.NavigationDrawerPresenter;
import by.toggi.rxbsuir.mvp.presenter.SchedulePresenter;
import by.toggi.rxbsuir.rest.BsuirService;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    private final RxBsuirApplication mApplication;

    public AppModule(RxBsuirApplication application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    RxBsuirApplication provideAppContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    SchedulePresenter provideSchedulePresenter(BsuirService service, StorIOSQLite storIOSQLite) {
        return new SchedulePresenter(service, storIOSQLite);
    }

    @Provides
    @Singleton
    NavigationDrawerPresenter provideNavigationDrawerPresenter(StorIOSQLite storIOSQLite) {
        return new NavigationDrawerPresenter(storIOSQLite);
    }

}
