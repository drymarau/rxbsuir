package by.toggi.rxbsuir.dagger.module;

import by.toggi.rxbsuir.dagger.PerApp;
import by.toggi.rxbsuir.rest.BsuirService;
import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

@Module public class BsuirServiceModule {

  private final String mEndpoint;

  public BsuirServiceModule(String endpoint) {
    mEndpoint = endpoint;
  }

  @Provides @PerApp RestAdapter provideRestAdapter() {
    return new RestAdapter.Builder().setEndpoint(mEndpoint).build();
  }

  @Provides @PerApp BsuirService provideBsuirService(RestAdapter restAdapter) {
    return restAdapter.create(BsuirService.class);
  }
}
