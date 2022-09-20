package by.toggi.rxbsuir.di

import com.squareup.workflow1.ui.ViewEnvironment
import com.squareup.workflow1.ui.ViewFactory
import com.squareup.workflow1.ui.ViewRegistry
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@OptIn(WorkflowUiExperimentalApi::class)
@Module
@InstallIn(SingletonComponent::class)
object ViewEnvironmentModule {

    @Provides
    @Singleton
    fun Set<@JvmSuppressWildcards ViewFactory<*>>.provide(): ViewEnvironment {
        val viewRegistry = ViewRegistry(*toTypedArray())
        return ViewEnvironment(mapOf(ViewRegistry to viewRegistry))
    }
}
