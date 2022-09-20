package by.toggi.rxbsuir.screen.studentgroups

import by.toggi.rxbsuir.api.BsuirClient
import by.toggi.rxbsuir.api.Employee
import by.toggi.rxbsuir.api.StudentGroup
import by.toggi.rxbsuir.screen.studentgroups.internal.StudentGroupsWorkflowImpl
import com.squareup.workflow1.testing.launchForTestingFromStartWith
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals

internal class StudentGroupsWorkflowTest {

    @Test
    fun `screen contains studentGroups`() {
        val studentGroups = listOf(
            StudentGroup(
                name = "${Random.nextInt()}",
                course = Random.nextInt()
            )
        )
        val client = object : BsuirClient {

            override suspend fun getStudentGroups(): List<StudentGroup> = studentGroups

            override suspend fun getEmployees(): List<Employee> = TODO()
        }
        val studentGroupsWorkflow = StudentGroupsWorkflowImpl(client)
        studentGroupsWorkflow.launchForTestingFromStartWith {
            val screen = awaitNextRendering()
            assertEquals(studentGroups, screen.studentGroups)
        }
    }

    @Test
    fun `onBackClick sets correct output`() {
        val client = object : BsuirClient {

            override suspend fun getStudentGroups(): List<StudentGroup> = emptyList()

            override suspend fun getEmployees(): List<Employee> = TODO()
        }
        val studentGroupsWorkflow = StudentGroupsWorkflowImpl(client)
        studentGroupsWorkflow.launchForTestingFromStartWith {
            val screen = awaitNextRendering()
            screen.onBackClick()
            assertEquals(StudentGroupsOutput.OnBack, awaitNextOutput())
        }
    }
}
