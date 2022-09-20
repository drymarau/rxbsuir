package by.toggi.rxbsuir.screen.studentgroups.internal

import android.os.Parcelable
import by.toggi.rxbsuir.api.BsuirClient
import by.toggi.rxbsuir.api.StudentGroup
import by.toggi.rxbsuir.screen.studentgroups.StudentGroupsOutput
import by.toggi.rxbsuir.screen.studentgroups.StudentGroupsScreen
import by.toggi.rxbsuir.screen.studentgroups.StudentGroupsWorkflow
import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.ui.toParcelable
import com.squareup.workflow1.ui.toSnapshot
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler
import javax.inject.Inject
import javax.inject.Singleton

@Parcelize
@TypeParceler<StudentGroup, StudentGroupParceler>
@JvmInline
internal value class StudentGroupsState(val studentGroups: List<StudentGroup> = emptyList()) :
    Parcelable

@Singleton
internal class StudentGroupsWorkflowImpl @Inject constructor(private val client: BsuirClient) :
    StudentGroupsWorkflow,
    StatefulWorkflow<Unit, StudentGroupsState, StudentGroupsOutput, StudentGroupsScreen>() {

    override fun initialState(props: Unit, snapshot: Snapshot?): StudentGroupsState =
        snapshot?.toParcelable() ?: StudentGroupsState()

    override fun snapshotState(state: StudentGroupsState): Snapshot = state.toSnapshot()

    override fun render(
        renderProps: Unit,
        renderState: StudentGroupsState,
        context: RenderContext
    ): StudentGroupsScreen {
        context.runningSideEffect("getStudentGroups") {
            val studentGroups = client.getStudentGroups()
            val action = OnStudentGroups(studentGroups)
            context.actionSink.send(action)
        }
        return StudentGroupsScreen(
            studentGroups = renderState.studentGroups,
            onBackClick = {
                context.actionSink.send(OnBackClick)
            }
        )
    }
}
