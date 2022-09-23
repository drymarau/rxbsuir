package by.toggi.rxbsuir.screen.studentgroups.internal

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import by.toggi.rxbsuir.api.BsuirClient
import by.toggi.rxbsuir.api.StudentGroup
import by.toggi.rxbsuir.screen.studentgroups.StudentGroupsOutput
import by.toggi.rxbsuir.screen.studentgroups.StudentGroupsScreen
import by.toggi.rxbsuir.screen.studentgroups.StudentGroupsWorkflow
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class StudentGroupsWorkflowImpl @Inject constructor(private val client: BsuirClient) :
    StudentGroupsWorkflow {

    @Composable
    override fun render(
        props: Unit,
        onOutput: (StudentGroupsOutput) -> Unit
    ): StudentGroupsScreen {
        var state by rememberSaveable { mutableStateOf(StudentGroupsState()) }
        LaunchedEffect(client) {
            state = StudentGroupsState(client.getStudentGroups())
        }
        return StudentGroupsScreen(
            studentGroups = state.studentGroups,
            onBackClick = {
                onOutput(StudentGroupsOutput.OnBack)
            }
        )
    }
}

@Parcelize
@TypeParceler<StudentGroup, StudentGroupParceler>
@JvmInline
private value class StudentGroupsState(val studentGroups: List<StudentGroup> = emptyList()) :
    Parcelable
