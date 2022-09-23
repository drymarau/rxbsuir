package by.toggi.rxbsuir.screen.studentgroups

import by.toggi.rxbsuir.api.BsuirClient
import by.toggi.rxbsuir.api.Employee
import by.toggi.rxbsuir.api.StudentGroup
import by.toggi.rxbsuir.screen.studentgroups.internal.StudentGroupsWorkflowImpl
import by.toggi.rxbsuir.workflow.test.awaitOutput
import by.toggi.rxbsuir.workflow.test.awaitRendering
import by.toggi.rxbsuir.workflow.test.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class StudentGroupsWorkflowTest {

    @Test
    fun `onBackClick outputs correct value`() = runTest {
        val workflow = StudentGroupsWorkflow { emptyList() }
        workflow.test {
            val rendering = awaitRendering()
            rendering.onBackClick()
            assertEquals(StudentGroupsOutput.OnBack, awaitOutput())
        }
    }

    @Test
    fun `studentGroups gets updated`() = runTest {
        val studentGroups = listOf(
            StudentGroup(name = "711801", course = 5),
            StudentGroup(name = "011801", course = 2)
        )
        val workflow = StudentGroupsWorkflow { studentGroups }
        workflow.test {
            var rendering = awaitRendering()
            assertEquals(emptyList(), rendering.studentGroups)
            rendering = awaitRendering()
            assertEquals(studentGroups, rendering.studentGroups)
        }
    }

    @Suppress("TestFunctionName")
    private inline fun StudentGroupsWorkflow(crossinline getStudentGroups: suspend () -> List<StudentGroup>): StudentGroupsWorkflow {
        val client = object : BsuirClient {
            override suspend fun getStudentGroups(): List<StudentGroup> = getStudentGroups()

            override suspend fun getEmployees(): List<Employee> = TODO()
        }
        return StudentGroupsWorkflowImpl(client)
    }
}
