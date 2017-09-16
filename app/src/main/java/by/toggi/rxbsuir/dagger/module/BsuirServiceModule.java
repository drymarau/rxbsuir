package by.toggi.rxbsuir.dagger.module;

import by.toggi.rxbsuir.dagger.PerApp;
import by.toggi.rxbsuir.rest.BsuirService;
import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module(includes = RetrofitModule.class) public class BsuirServiceModule {

  @Provides @PerApp BsuirService provide(Retrofit retrofit) {
    return retrofit.create(BsuirService.class);
  }
}
