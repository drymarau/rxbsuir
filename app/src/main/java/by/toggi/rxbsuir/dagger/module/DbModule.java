package by.toggi.rxbsuir.dagger.module;

import android.app.Application;

import by.toggi.rxbsuir.dagger.PerApp;
import by.toggi.rxbsuir.db.RxBsuirOpenHelper;
import dagger.Module;
import dagger.Provides;

@Module public class DbModule {

  @Provides @PerApp RxBsuirOpenHelper provideOpenHelper(Application application) {
    return new RxBsuirOpenHelper(application);
  }
}
