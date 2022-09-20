package by.toggi.rxbsuir.screen.home.internal

import by.toggi.rxbsuir.screen.home.HomeWorkflow
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface HomeWorkflowModule {

    @Binds
    fun HomeWorkflowImpl.bindHomeWorkflow(): HomeWorkflow
}
