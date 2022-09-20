package by.toggi.rxbsuir.screen.studentgroups.internal

import by.toggi.rxbsuir.screen.studentgroups.StudentGroupsScreen
import com.squareup.workflow1.ui.ViewFactory
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.compose.composeViewFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet

@Module
@InstallIn(SingletonComponent::class)
internal object StudentGroupsViewFactoryModule {

    @OptIn(WorkflowUiExperimentalApi::class)
    @Provides
    @IntoSet
    fun provide(): ViewFactory<*> = composeViewFactory<StudentGroupsScreen> { rendering, _ ->
        StudentGroupsScreenRenderer(
            studentGroups = rendering.studentGroups,
            onBackClick = rendering.onBackClick
        )
    }
}
