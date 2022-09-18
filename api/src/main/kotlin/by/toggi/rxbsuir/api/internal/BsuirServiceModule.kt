package by.toggi.rxbsuir.api.internal

import by.toggi.rxbsuir.api.BsuirClient
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface BsuirServiceModule {

    @Binds
    fun OkHttpClientBsuirClient.bindBsuirService(): BsuirClient
}
