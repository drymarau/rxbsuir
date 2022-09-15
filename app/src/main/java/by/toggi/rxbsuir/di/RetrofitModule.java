package by.toggi.rxbsuir.di;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

@Module
@InstallIn(SingletonComponent.class)
public class RetrofitModule {

    private static final String URL = "https://journal.bsuir.by/";

    @Provides
    @Singleton
    Retrofit provide(OkHttpClient client,
                     @Named("rxjava") CallAdapter.Factory callAdapterFactory,
                     @Named("gson") Converter.Factory converterFactory) {
        return new Retrofit.Builder()
                .baseUrl(URL)
                .client(client)
                .addCallAdapterFactory(callAdapterFactory)
                .addConverterFactory(converterFactory)
                .build();
    }
}
