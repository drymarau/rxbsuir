package by.toggi.rxbsuir.dagger.module;

import javax.inject.Named;

import by.toggi.rxbsuir.dagger.PerApp;
import dagger.Module;
import dagger.Provides;
import retrofit2.CallAdapter;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

@Module public class RxJavaCallAdapterFactoryModule {

  @Provides @Named("rxjava") @PerApp CallAdapter.Factory provide() {
    return RxJavaCallAdapterFactory.createAsync();
  }
}
