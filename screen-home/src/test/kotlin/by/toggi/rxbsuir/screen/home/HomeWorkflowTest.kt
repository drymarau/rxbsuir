package by.toggi.rxbsuir.screen.home

import by.toggi.rxbsuir.screen.home.internal.HomeWorkflowImpl
import com.squareup.workflow1.testing.launchForTestingFromStartWith
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

internal class HomeWorkflowTest {

    private lateinit var homeWorkflow: HomeWorkflow

    @BeforeTest
    fun setUp() {
        homeWorkflow = HomeWorkflowImpl()
    }

    @Test
    fun `onStudentGroupsClick sets correct output`() {
        homeWorkflow.launchForTestingFromStartWith {
            val screen = awaitNextRendering()
            screen.onStudentGroupsClick()
            assertEquals(HomeOutput.OnStudentGroups, awaitNextOutput())
        }
    }

    @Test
    fun `onEmployeesClick sets correct output`() {
        homeWorkflow.launchForTestingFromStartWith {
            val screen = awaitNextRendering()
            screen.onEmployeesClick()
            assertEquals(HomeOutput.OnEmployees, awaitNextOutput())
        }
    }

    @Test
    fun `onBackClick sets correct output`() {
        homeWorkflow.launchForTestingFromStartWith {
            val screen = awaitNextRendering()
            screen.onBackClick()
            assertEquals(HomeOutput.OnBack, awaitNextOutput())
        }
    }
}
