package by.toggi.rxbsuir.screen.home.internal

import androidx.compose.runtime.Composable
import by.toggi.rxbsuir.screen.home.HomeOutput
import by.toggi.rxbsuir.screen.home.HomeScreen
import by.toggi.rxbsuir.screen.home.HomeWorkflow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class HomeWorkflowImpl @Inject constructor() : HomeWorkflow {

    @Composable
    override fun render(props: Unit, onOutput: (HomeOutput) -> Unit): HomeScreen = HomeScreen(
        onStudentGroupsClick = {
            onOutput(HomeOutput.OnStudentGroups)
        },
        onEmployeesClick = {
            onOutput(HomeOutput.OnEmployees)
        },
        onBackClick = {
            onOutput(HomeOutput.OnBack)
        }
    )
}
