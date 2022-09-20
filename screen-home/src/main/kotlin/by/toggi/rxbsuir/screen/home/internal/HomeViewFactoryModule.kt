package by.toggi.rxbsuir.screen.home.internal

import by.toggi.rxbsuir.screen.home.HomeScreen
import com.squareup.workflow1.ui.ViewFactory
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.compose.composeViewFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object HomeViewFactoryModule {

    @OptIn(WorkflowUiExperimentalApi::class)
    @Provides
    @Singleton
    @IntoSet
    fun provide(): ViewFactory<*> = composeViewFactory<HomeScreen> { rendering, _ ->
        HomeScreenRenderer(
            onStudentGroupsClick = rendering.onStudentGroupsClick,
            onEmployeesClick = rendering.onEmployeesClick,
            onBackClick = rendering.onBackClick
        )
    }
}
