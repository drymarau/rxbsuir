package by.toggi.rxbsuir.dagger.module;

import javax.inject.Named;

import by.toggi.rxbsuir.dagger.PerApp;
import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

@Module(includes = { HttpLoggingInterceptorModule.class, OkHttpCacheModule.class })
public class OkHttpClientModule {

  @Provides @PerApp OkHttpClient provide(@Named("logging") Interceptor interceptor, Cache cache) {
    return new OkHttpClient.Builder().cache(cache).addInterceptor(interceptor).build();
  }
}
