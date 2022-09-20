package by.toggi.rxbsuir

import android.os.Parcelable
import by.toggi.rxbsuir.screen.home.HomeOutput
import by.toggi.rxbsuir.screen.home.HomeWorkflow
import by.toggi.rxbsuir.screen.studentgroups.StudentGroupsOutput
import by.toggi.rxbsuir.screen.studentgroups.StudentGroupsWorkflow
import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.WorkflowAction
import com.squareup.workflow1.renderChild
import com.squareup.workflow1.ui.toParcelable
import com.squareup.workflow1.ui.toSnapshot
import kotlinx.parcelize.Parcelize
import javax.inject.Inject
import javax.inject.Singleton

sealed interface RxBsuirScreen : Parcelable {

    @Parcelize
    object Home : RxBsuirScreen

    @Parcelize
    object StudentGroups : RxBsuirScreen
}

@Parcelize
@JvmInline
value class RxBsuirState(val screen: RxBsuirScreen = RxBsuirScreen.Home) : Parcelable

sealed interface RxBsuirOutput {

    object OnBack : RxBsuirOutput
}

typealias RxBsuirWorkflowAction = WorkflowAction<Unit, RxBsuirState, RxBsuirOutput>

class OnHomeOutput(private val output: HomeOutput) : RxBsuirWorkflowAction() {

    override fun Updater.apply() {
        when (output) {
            is HomeOutput.OnStudentGroups -> state = RxBsuirState(RxBsuirScreen.StudentGroups)
            is HomeOutput.OnEmployees -> {
            }
            is HomeOutput.OnBack -> setOutput(RxBsuirOutput.OnBack)
        }
    }
}

class OnStudentGroupsOutput(private val output: StudentGroupsOutput) : RxBsuirWorkflowAction() {

    override fun Updater.apply() {
        when (output) {
            StudentGroupsOutput.OnBack -> state = RxBsuirState(RxBsuirScreen.Home)
        }
    }
}

@Singleton
class RxBsuirWorkflow @Inject constructor(
    private val homeWorkflow: HomeWorkflow,
    private val studentGroupsWorkflow: StudentGroupsWorkflow
) : StatefulWorkflow<Unit, RxBsuirState, RxBsuirOutput, Any>() {

    override fun initialState(props: Unit, snapshot: Snapshot?): RxBsuirState =
        snapshot?.toParcelable() ?: RxBsuirState()

    override fun snapshotState(state: RxBsuirState): Snapshot = state.toSnapshot()

    override fun render(
        renderProps: Unit,
        renderState: RxBsuirState,
        context: RenderContext
    ): Any = when (renderState.screen) {
        is RxBsuirScreen.Home -> context.renderChild(
            child = homeWorkflow,
            handler = ::OnHomeOutput
        )
        is RxBsuirScreen.StudentGroups -> context.renderChild(
            child = studentGroupsWorkflow,
            handler = ::OnStudentGroupsOutput
        )
    }
}
