package by.toggi.rxbsuir.dagger.module;

import by.toggi.rxbsuir.dagger.PerApp;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

@Module public class HttpLoggingInterceptorModule {

  private static final HttpLoggingInterceptor.Logger LOGGER =
      message -> Timber.tag("OkHttp").d(message);

  @Provides @Named("logging") @PerApp Interceptor provide(@Named("debug") boolean debug) {
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(LOGGER);
    if (debug) {
      interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    } else {
      interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
    }
    return interceptor;
  }
}
