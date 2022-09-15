package by.toggi.rxbsuir.di;

import com.google.gson.Gson;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Converter;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class GsonConverterFactoryModule {

    @Provides
    @Singleton
    @Named("gson")
    Converter.Factory provide(Gson gson) {
        return GsonConverterFactory.create(gson);
    }
}
