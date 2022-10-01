package by.toggi.rxbsuir.screen.employees

import by.toggi.rxbsuir.api.Announcement
import by.toggi.rxbsuir.api.BsuirClient
import by.toggi.rxbsuir.api.Employee
import by.toggi.rxbsuir.api.StudentGroup
import by.toggi.rxbsuir.screen.employees.internal.EmployeesWorkflowImpl
import by.toggi.rxbsuir.workflow.test.awaitOutput
import by.toggi.rxbsuir.workflow.test.awaitRendering
import by.toggi.rxbsuir.workflow.test.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class EmployeesWorkflowTest {

    @Test
    fun `onBackClick outputs correct value`() = runTest {
        val workflow = EmployeesWorkflow { emptyList() }
        workflow.test {
            val rendering = awaitRendering()
            rendering.onBackClick()
            assertEquals(EmployeesOutput.OnBack, awaitOutput())
        }
    }

    @Test
    fun `employees get updated`() = runTest {
        val employees = listOf(
            Employee(
                id = 1,
                urlId = "ivanou-i-i",
                lastName = "Ivanou",
                firstName = "Ivan",
                middleName = "Ivanavich",
                photoLink = "https://example.com/employees/ivanou-i-i",
                degree = "Master",
                rank = "Assistant"
            )
        )
        val workflow = EmployeesWorkflow { employees }
        workflow.test {
            var rendering = awaitRendering()
            assertEquals(emptyList(), rendering.employees)
            rendering = awaitRendering()
            assertEquals(employees, rendering.employees)
        }
    }

    @Suppress("TestFunctionName")
    private inline fun EmployeesWorkflow(crossinline getEmployees: suspend () -> List<Employee>): EmployeesWorkflow {
        val client = object : BsuirClient {
            override suspend fun getStudentGroups(): List<StudentGroup> = TODO()

            override suspend fun getEmployees(): List<Employee> = getEmployees()

            override suspend fun getAnnouncements(urlId: String): List<Announcement> = TODO()
        }
        return EmployeesWorkflowImpl(client)
    }
}
