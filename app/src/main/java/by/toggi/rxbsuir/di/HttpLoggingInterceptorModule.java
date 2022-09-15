package by.toggi.rxbsuir.di;

import javax.inject.Named;
import javax.inject.Singleton;

import by.toggi.rxbsuir.BuildConfig;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

@Module
@InstallIn(SingletonComponent.class)
public class HttpLoggingInterceptorModule {

    private static final HttpLoggingInterceptor.Logger LOGGER = message -> Timber.tag("OkHttp").d(message);

    @Provides
    @Singleton
    @Named("logging")
    Interceptor provide() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(LOGGER);
        if (BuildConfig.DEBUG) {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        }
        return interceptor;
    }
}
