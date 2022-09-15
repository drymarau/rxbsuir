package by.toggi.rxbsuir.dagger.module;

import com.google.gson.Gson;

import javax.inject.Named;

import by.toggi.rxbsuir.dagger.PerApp;
import dagger.Module;
import dagger.Provides;
import retrofit2.Converter;
import retrofit2.converter.gson.GsonConverterFactory;

@Module public class GsonConverterFactoryModule {

  @Provides @Named("gson") @PerApp Converter.Factory provide(Gson gson) {
    return GsonConverterFactory.create(gson);
  }
}
