package by.toggi.rxbsuir.dagger.module;

import by.toggi.rxbsuir.dagger.PerApp;
import by.toggi.rxbsuir.rest.BsuirService;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import retrofit.RestAdapter;

@Module public class BsuirServiceModule {

  @Provides @PerApp RestAdapter provideRestAdapter(@Named("bsuir") String url) {
    return new RestAdapter.Builder().setEndpoint(url).build();
  }

  @Provides @PerApp BsuirService provideBsuirService(RestAdapter restAdapter) {
    return restAdapter.create(BsuirService.class);
  }
}
