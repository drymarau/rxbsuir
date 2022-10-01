package by.toggi.rxbsuir.screen.employees.internal

import android.os.Parcelable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import by.toggi.rxbsuir.api.BsuirClient
import by.toggi.rxbsuir.api.Employee
import by.toggi.rxbsuir.screen.employees.EmployeeParceler
import by.toggi.rxbsuir.screen.employees.EmployeesOutput
import by.toggi.rxbsuir.screen.employees.EmployeesScreen
import by.toggi.rxbsuir.screen.employees.EmployeesWorkflow
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.TypeParceler
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class EmployeesWorkflowImpl @Inject constructor(private val client: BsuirClient) :
    EmployeesWorkflow {

    @Composable
    override fun render(props: Unit, onOutput: (EmployeesOutput) -> Unit): EmployeesScreen {
        var state by rememberSaveable { mutableStateOf(EmployeesState()) }
        GetEmployeesEffect { state = EmployeesState(it) }
        return EmployeesScreen(
            employees = state.employees,
            onEmployeeClick = {
                onOutput(EmployeesOutput.OnEmployee(it))
            },
            onBackClick = {
                onOutput(EmployeesOutput.OnBack)
            }
        )
    }

    @Composable
    private fun GetEmployeesEffect(onEmployees: (List<Employee>) -> Unit) {
        val currentOnEmployees by rememberUpdatedState(onEmployees)
        LaunchedEffect(client) {
            val employees = try {
                client.getEmployees()
            } catch (t: Throwable) {
                emptyList()
            }
            currentOnEmployees(employees)
        }
    }
}

@TypeParceler<Employee, EmployeeParceler>
@Parcelize
@JvmInline
private value class EmployeesState(val employees: List<Employee> = emptyList()) : Parcelable
