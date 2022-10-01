package by.toggi.rxbsuir

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import by.toggi.rxbsuir.screen.announcements.AnnouncementsOutput
import by.toggi.rxbsuir.screen.announcements.AnnouncementsProps
import by.toggi.rxbsuir.screen.announcements.AnnouncementsWorkflow
import by.toggi.rxbsuir.screen.employees.EmployeesOutput
import by.toggi.rxbsuir.screen.employees.EmployeesWorkflow
import by.toggi.rxbsuir.screen.home.HomeOutput
import by.toggi.rxbsuir.screen.home.HomeWorkflow
import by.toggi.rxbsuir.screen.studentgroups.StudentGroupsOutput
import by.toggi.rxbsuir.screen.studentgroups.StudentGroupsWorkflow
import by.toggi.rxbsuir.workflow.Workflow
import by.toggi.rxbsuir.workflow.render
import kotlinx.parcelize.Parcelize
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RxBsuirWorkflow @Inject constructor(
    private val homeWorkflow: HomeWorkflow,
    private val employeesWorkflow: EmployeesWorkflow,
    private val studentGroupsWorkflow: StudentGroupsWorkflow,
    private val announcementsWorkflow: AnnouncementsWorkflow,
) : Workflow<Unit, RxBsuirOutput, Any> {

    @Composable
    override fun render(props: Unit, onOutput: (RxBsuirOutput) -> Unit): Any {
        val (state, setState) = rememberSaveable { mutableStateOf(RxBsuirState()) }
        return when (val screen = state.screen) {
            is RxBsuirScreen.Home -> homeWorkflow.render { output ->
                onHomeOutput(output, setState, onOutput)
            }
            is RxBsuirScreen.Employees -> employeesWorkflow.render { output ->
                onEmployeesOutput(output, setState)
            }
            is RxBsuirScreen.StudentGroups -> studentGroupsWorkflow.render { output ->
                onStudentGroupsOutput(output, setState)
            }
            is RxBsuirScreen.Announcements -> announcementsWorkflow.render(
                props = AnnouncementsProps(screen.employee),
                onOutput = { output -> onAnnouncementsOutput(output, setState) }
            )
        }
    }
}

@Parcelize
@JvmInline
private value class RxBsuirState(val screen: RxBsuirScreen = RxBsuirScreen.Home) : Parcelable

private inline fun onHomeOutput(
    output: HomeOutput,
    setState: (RxBsuirState) -> Unit,
    onOutput: (RxBsuirOutput) -> Unit,
) = when (output) {
    is HomeOutput.OnStudentGroups -> setState(RxBsuirState(RxBsuirScreen.StudentGroups))
    is HomeOutput.OnEmployees -> setState(RxBsuirState(RxBsuirScreen.Employees))
    is HomeOutput.OnBack -> onOutput(RxBsuirOutput.OnBack)
}

private inline fun onEmployeesOutput(
    output: EmployeesOutput,
    setState: (RxBsuirState) -> Unit,
) {
    val screen = when (output) {
        is EmployeesOutput.OnEmployee -> RxBsuirScreen.Announcements(output.employee)
        is EmployeesOutput.OnBack -> RxBsuirScreen.Home
    }
    setState(RxBsuirState(screen))
}

private inline fun onStudentGroupsOutput(
    output: StudentGroupsOutput,
    setState: (RxBsuirState) -> Unit,
) = when (output) {
    is StudentGroupsOutput.OnBack -> setState(RxBsuirState(RxBsuirScreen.Home))
}

private inline fun onAnnouncementsOutput(
    output: AnnouncementsOutput,
    setState: (RxBsuirState) -> Unit,
) = when (output) {
    is AnnouncementsOutput.OnBack -> {
        setState(RxBsuirState(RxBsuirScreen.Employees))
    }
}
