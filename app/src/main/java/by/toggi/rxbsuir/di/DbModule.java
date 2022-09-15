package by.toggi.rxbsuir.di;

import android.app.Application;

import javax.inject.Singleton;

import by.toggi.rxbsuir.db.RxBsuirOpenHelper;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class DbModule {

    @Provides
    @Singleton
    RxBsuirOpenHelper provideOpenHelper(Application application) {
        return new RxBsuirOpenHelper(application);
    }
}
