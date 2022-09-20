package by.toggi.rxbsuir.screen.studentgroups

import by.toggi.rxbsuir.api.StudentGroup
import com.squareup.workflow1.Workflow

public sealed interface StudentGroupsOutput {

    public object OnBack : StudentGroupsOutput
}

public data class StudentGroupsScreen(
    val studentGroups: List<StudentGroup>,
    val onBackClick: () -> Unit
)

public interface StudentGroupsWorkflow : Workflow<Unit, StudentGroupsOutput, StudentGroupsScreen>
