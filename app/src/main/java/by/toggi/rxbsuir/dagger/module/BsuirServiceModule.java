package by.toggi.rxbsuir.dagger.module;

import by.toggi.rxbsuir.dagger.PerApp;
import by.toggi.rxbsuir.rest.BsuirService;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import retrofit.RestAdapter;
import timber.log.Timber;

@Module public class BsuirServiceModule {

  @Provides @PerApp RestAdapter provideRestAdapter(@Named("bsuir") String url,
      @Named("debug") boolean debug) {
    return new RestAdapter.Builder().setEndpoint(url)
        .setLog(message -> Timber.tag("Retrofit").d(message))
        .setLogLevel(debug ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.BASIC)
        .build();
  }

  @Provides @PerApp BsuirService provideBsuirService(RestAdapter restAdapter) {
    return restAdapter.create(BsuirService.class);
  }
}
