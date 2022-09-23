package by.toggi.rxbsuir

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
    private val studentGroupsWorkflow: StudentGroupsWorkflow
) : Workflow<Unit, RxBsuirOutput, Any> {

    @Composable
    override fun render(props: Unit, onOutput: (RxBsuirOutput) -> Unit): Any {
        var state by rememberSaveable { mutableStateOf(RxBsuirState()) }
        return when (state.screen) {
            is RxBsuirScreen.Home -> homeWorkflow.render {
                when (it) {
                    is HomeOutput.OnStudentGroups -> {
                        state = RxBsuirState(RxBsuirScreen.StudentGroups)
                    }
                    is HomeOutput.OnEmployees -> {}
                    is HomeOutput.OnBack -> onOutput(RxBsuirOutput.OnBack)
                }
            }
            is RxBsuirScreen.StudentGroups -> studentGroupsWorkflow.render {
                when (it) {
                    StudentGroupsOutput.OnBack -> state = RxBsuirState(RxBsuirScreen.Home)
                }
            }
        }
    }
}

@Parcelize
@JvmInline
private value class RxBsuirState(val screen: RxBsuirScreen = RxBsuirScreen.Home) : Parcelable
