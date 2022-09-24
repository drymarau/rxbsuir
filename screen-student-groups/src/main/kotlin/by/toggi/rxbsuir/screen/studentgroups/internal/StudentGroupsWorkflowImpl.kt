package by.toggi.rxbsuir.screen.studentgroups.internal

import android.os.Parcel
import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import by.toggi.rxbsuir.api.BsuirClient
import by.toggi.rxbsuir.api.StudentGroup
import by.toggi.rxbsuir.screen.studentgroups.StudentGroupsOutput
import by.toggi.rxbsuir.screen.studentgroups.StudentGroupsScreen
import by.toggi.rxbsuir.screen.studentgroups.StudentGroupsWorkflow
import kotlinx.parcelize.Parceler
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class StudentGroupsWorkflowImpl @Inject constructor(private val client: BsuirClient) :
    StudentGroupsWorkflow {

    @Composable
    override fun render(props: Unit, onOutput: (StudentGroupsOutput) -> Unit): StudentGroupsScreen {
        var state by rememberSaveable { mutableStateOf(StudentGroupsState()) }
        GetStudentGroupsEffect { state = StudentGroupsState(it) }
        return StudentGroupsScreen(
            studentGroups = state.studentGroups,
            onBackClick = {
                onOutput(StudentGroupsOutput.OnBack)
            }
        )
    }

    @Composable
    private fun GetStudentGroupsEffect(onStudentGroups: (List<StudentGroup>) -> Unit) {
        val currentOnStudentGroups by rememberUpdatedState(onStudentGroups)
        LaunchedEffect(client) {
            val studentGroups = try {
                client.getStudentGroups()
            } catch (t: Throwable) {
                emptyList()
            }
            currentOnStudentGroups(studentGroups)
        }
    }
}

@Parcelize
@TypeParceler<StudentGroup, StudentGroupParceler>
@JvmInline
private value class StudentGroupsState(val studentGroups: List<StudentGroup> = emptyList()) :
    Parcelable

private object StudentGroupParceler : Parceler<StudentGroup> {

    override fun create(parcel: Parcel): StudentGroup = StudentGroup(
        id = parcel.readLong(),
        name = parcel.readString()!!,
        course = when (val course = parcel.readInt()) {
            -1 -> null
            else -> course
        }
    )

    override fun StudentGroup.write(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeInt(course ?: -1)
    }
}
