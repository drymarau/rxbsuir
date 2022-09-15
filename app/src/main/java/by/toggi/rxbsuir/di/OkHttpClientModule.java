package by.toggi.rxbsuir.di;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

@Module
@InstallIn(SingletonComponent.class)
public class OkHttpClientModule {

    @Provides
    @Singleton
    OkHttpClient provide(@Named("logging") Interceptor interceptor, Cache cache) {
        return new OkHttpClient.Builder().cache(cache).addInterceptor(interceptor).build();
    }
}
