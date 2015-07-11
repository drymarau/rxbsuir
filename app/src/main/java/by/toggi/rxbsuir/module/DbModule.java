package by.toggi.rxbsuir.module;

import javax.inject.Singleton;

import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.db.RxBsuirOpenHelper;
import dagger.Module;
import dagger.Provides;

@Module
public class DbModule {

    @Provides
    @Singleton
    RxBsuirOpenHelper provideOpenHelper(RxBsuirApplication application) {
        return new RxBsuirOpenHelper(application);
    }

}
