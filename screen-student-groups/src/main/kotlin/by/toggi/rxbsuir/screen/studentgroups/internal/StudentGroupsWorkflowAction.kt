package by.toggi.rxbsuir.screen.studentgroups.internal

import by.toggi.rxbsuir.api.StudentGroup
import by.toggi.rxbsuir.screen.studentgroups.StudentGroupsOutput
import com.squareup.workflow1.WorkflowAction

internal typealias StudentGroupsWorkflowAction = WorkflowAction<Unit, StudentGroupsState, StudentGroupsOutput>

internal class OnStudentGroups(private val studentGroups: List<StudentGroup>) :
    StudentGroupsWorkflowAction() {

    override fun Updater.apply() {
        state = StudentGroupsState(studentGroups)
    }
}

internal object OnBackClick : StudentGroupsWorkflowAction() {

    override fun Updater.apply() {
        setOutput(StudentGroupsOutput.OnBack)
    }
}
