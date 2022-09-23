package by.toggi.rxbsuir.screen.home

import by.toggi.rxbsuir.screen.home.internal.HomeWorkflowImpl
import by.toggi.rxbsuir.workflow.test.awaitOutput
import by.toggi.rxbsuir.workflow.test.awaitRendering
import by.toggi.rxbsuir.workflow.test.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class HomeWorkflowTest {

    private lateinit var workflow: HomeWorkflow

    @BeforeTest
    fun setUp() {
        workflow = HomeWorkflowImpl()
    }

    @Test
    fun `onStudentGroupsClick outputs correct value`() = runTest {
        workflow.test {
            val rendering = awaitRendering()
            rendering.onStudentGroupsClick()
            assertEquals(HomeOutput.OnStudentGroups, awaitOutput())
        }
    }

    @Test
    fun `onEmployeesClick outputs correct value`() = runTest {
        workflow.test {
            val rendering = awaitRendering()
            rendering.onEmployeesClick()
            assertEquals(HomeOutput.OnEmployees, awaitOutput())
        }
    }

    @Test
    fun `onBackClick outputs correct value`() = runTest {
        workflow.test {
            val rendering = awaitRendering()
            rendering.onBackClick()
            assertEquals(HomeOutput.OnBack, awaitOutput())
        }
    }
}
