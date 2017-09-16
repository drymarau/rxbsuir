package by.toggi.rxbsuir.dagger.module;

import by.toggi.rxbsuir.dagger.PerApp;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

@Module(includes = { RxJavaCallAdapterFactoryModule.class, GsonConverterFactoryModule.class })
public class RetrofitModule {

  @Provides @PerApp Retrofit provide(@Named("bsuir") String url, OkHttpClient client,
      @Named("rxjava") CallAdapter.Factory callAdapterFactory,
      @Named("gson") Converter.Factory converterFactory) {
    return new Retrofit.Builder().baseUrl(url)
        .client(client)
        .addCallAdapterFactory(callAdapterFactory)
        .addConverterFactory(converterFactory)
        .build();
  }
}
