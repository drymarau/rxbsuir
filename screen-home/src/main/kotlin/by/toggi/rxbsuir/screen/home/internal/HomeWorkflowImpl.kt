package by.toggi.rxbsuir.screen.home.internal

import by.toggi.rxbsuir.screen.home.HomeOutput
import by.toggi.rxbsuir.screen.home.HomeScreen
import by.toggi.rxbsuir.screen.home.HomeWorkflow
import com.squareup.workflow1.StatelessWorkflow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class HomeWorkflowImpl @Inject constructor() :
    HomeWorkflow, StatelessWorkflow<Unit, HomeOutput, HomeScreen>() {

    override fun render(renderProps: Unit, context: RenderContext): HomeScreen = HomeScreen(
        onStudentGroupsClick = {
            context.actionSink.send(OnStudentGroupsClick)
        },
        onEmployeesClick = context.eventHandler {
            context.actionSink.send(OnEmployeesClick)
        },
        onBackClick = context.eventHandler {
            context.actionSink.send(OnBackClick)
        }
    )
}
