package by.toggi.rxbsuir.screen.home

import com.squareup.workflow1.Workflow

public sealed interface HomeOutput {

    public object OnStudentGroups : HomeOutput
    public object OnEmployees : HomeOutput
    public object OnBack : HomeOutput
}

public data class HomeScreen(
    val onStudentGroupsClick: () -> Unit,
    val onEmployeesClick: () -> Unit,
    val onBackClick: () -> Unit
)

public interface HomeWorkflow : Workflow<Unit, HomeOutput, HomeScreen>
