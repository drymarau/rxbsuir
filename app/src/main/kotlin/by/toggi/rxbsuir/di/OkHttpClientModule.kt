package by.toggi.rxbsuir.di

import android.util.Log
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OkHttpClientModule {

    @Provides
    @Singleton
    fun provide(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor { Log.v("OkHttpClient", it) }
        interceptor.level = HttpLoggingInterceptor.Level.BASIC
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }
}
