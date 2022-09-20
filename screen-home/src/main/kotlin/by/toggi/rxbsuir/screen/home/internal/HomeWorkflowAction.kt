package by.toggi.rxbsuir.screen.home.internal

import by.toggi.rxbsuir.screen.home.HomeOutput
import com.squareup.workflow1.WorkflowAction

internal typealias HomeWorkflowAction = WorkflowAction<Unit, Nothing, HomeOutput>

internal object OnStudentGroupsClick : HomeWorkflowAction() {

    override fun Updater.apply() {
        setOutput(HomeOutput.OnStudentGroups)
    }
}

internal object OnEmployeesClick : HomeWorkflowAction() {

    override fun Updater.apply() {
        setOutput(HomeOutput.OnEmployees)
    }
}

internal object OnBackClick : HomeWorkflowAction() {

    override fun Updater.apply() {
        setOutput(HomeOutput.OnBack)
    }
}
