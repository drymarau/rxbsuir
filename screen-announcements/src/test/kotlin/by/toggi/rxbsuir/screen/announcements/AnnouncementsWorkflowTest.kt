package by.toggi.rxbsuir.screen.announcements

import by.toggi.rxbsuir.api.Announcement
import by.toggi.rxbsuir.api.BsuirClient
import by.toggi.rxbsuir.api.Employee
import by.toggi.rxbsuir.api.StudentGroup
import by.toggi.rxbsuir.screen.announcements.internal.AnnouncementsWorkflowImpl
import by.toggi.rxbsuir.workflow.test.awaitOutput
import by.toggi.rxbsuir.workflow.test.awaitRendering
import by.toggi.rxbsuir.workflow.test.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlin.random.Random
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class AnnouncementsWorkflowTest {

    private lateinit var employee: Employee

    @BeforeTest
    fun setUp() {
        employee = with(Random) {
            Employee(
                id = nextLong(),
                urlId = "${nextInt()}",
                lastName = "${nextInt()}",
                firstName = "${nextInt()}",
                middleName = "${nextInt()}",
                photoLink = "${nextInt()}",
                degree = "${nextInt()}",
                rank = "${nextInt()}",
            )
        }
    }

    @Test
    fun `onBackClick outputs correct value`() = runTest {
        val workflow = AnnouncementsWorkflow {
            assertEquals(it, employee.urlId)
            emptyList()
        }
        workflow.test(AnnouncementsProps(employee)) {
            val rendering = awaitRendering()
            rendering.onBackClick()
            assertEquals(AnnouncementsOutput.OnBack, awaitOutput())
        }
    }

    @Test
    fun `announcements get updated`() = runTest {
        val announcements = listOf(
            with(Random) {
                Announcement(
                    id = nextLong(),
                    content = "${nextInt()}",
                    date = LocalDate.fromEpochDays(0),
                    startTime = LocalTime.fromSecondOfDay(0),
                    endTime = LocalTime.fromSecondOfDay(3600),
                )
            }
        )
        val workflow = AnnouncementsWorkflow {
            assertEquals(it, employee.urlId)
            announcements
        }
        workflow.test(AnnouncementsProps(employee)) {
            var rendering = awaitRendering()
            assertEquals(emptyList(), rendering.announcements)
            rendering = awaitRendering()
            assertEquals(announcements, rendering.announcements)
        }
    }

    @Suppress("TestFunctionName")
    private inline fun AnnouncementsWorkflow(crossinline getAnnouncements: suspend (String) -> List<Announcement>): AnnouncementsWorkflow {
        val client = object : BsuirClient {
            override suspend fun getStudentGroups(): List<StudentGroup> = TODO()

            override suspend fun getEmployees(): List<Employee> = TODO()

            override suspend fun getAnnouncements(urlId: String): List<Announcement> =
                getAnnouncements(urlId)
        }
        return AnnouncementsWorkflowImpl(client)
    }
}
