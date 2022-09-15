package by.toggi.rxbsuir.di;

import javax.inject.Singleton;

import by.toggi.rxbsuir.rest.BsuirService;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Retrofit;

@Module
@InstallIn(SingletonComponent.class)
public class BsuirServiceModule {

    @Provides
    @Singleton
    BsuirService provide(Retrofit retrofit) {
        return retrofit.create(BsuirService.class);
    }
}
