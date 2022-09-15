package by.toggi.rxbsuir.dagger.module;

import android.app.Application;

import java.io.File;

import by.toggi.rxbsuir.dagger.PerApp;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;

@SuppressWarnings("ResultOfMethodCallIgnored") @Module public class OkHttpCacheModule {

  private static final String NAME = "responses";
  private static final long SIZE = 5 * 1024 * 1024;

  @Provides @PerApp Cache provide(Application application) {
    File directory = new File(application.getCacheDir(), NAME);
    if (!directory.exists()) {
      directory.mkdirs();
    }
    return new Cache(directory, SIZE);
  }
}
