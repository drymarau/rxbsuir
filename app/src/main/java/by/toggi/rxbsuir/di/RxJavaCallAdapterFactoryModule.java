package by.toggi.rxbsuir.di;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import retrofit2.CallAdapter;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class RxJavaCallAdapterFactoryModule {

    @Provides
    @Singleton
    @Named("rxjava")
    CallAdapter.Factory provide() {
        return RxJavaCallAdapterFactory.createAsync();
    }
}
