package by.toggi.rxbsuir.dagger.module;

import by.toggi.rxbsuir.dagger.PerApp;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import retrofit2.CallAdapter;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

@Module public class RxJavaCallAdapterFactoryModule {

  @Provides @Named("rxjava") @PerApp CallAdapter.Factory provide() {
    return RxJavaCallAdapterFactory.createAsync();
  }
}
