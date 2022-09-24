package by.toggi.rxbsuir.api

import by.toggi.rxbsuir.api.internal.HttpUrl
import by.toggi.rxbsuir.api.internal.OkHttpClientBsuirClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import okio.FileSystem
import okio.Path.Companion.toPath
import org.junit.Rule
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@OptIn(ExperimentalCoroutinesApi::class)
class BsuirServiceTest {

    @get:Rule
    val server = MockWebServer()

    private lateinit var url: HttpUrl
    private lateinit var client: BsuirClient

    @BeforeTest
    fun setUp() {
        url = server.url("/")
        client = OkHttpClientBsuirClient(
            client = OkHttpClient(),
            json = Json { ignoreUnknownKeys = true },
            url = url
        )
    }

    @Test
    fun `getStudentGroups throws on non-200 code`() = runTest {
        server.enqueue { setResponseCode(404) }
        assertFailsWith<IllegalStateException> { client.getStudentGroups() }
        server.takeRequest(::assertGetStudentGroups)
    }

    @Test
    fun `getStudentGroups returns a list of groups`() = runTest {
        server.enqueue {
            setResponseCode(200)
            setBody(read("getStudentGroups.json"))
        }
        assertEquals(
            expected = listOf(
                StudentGroup(
                    id = 23277,
                    name = "010101",
                    course = 3
                ),
                StudentGroup(
                    id = 23288,
                    name = "010201",
                    course = 3
                )
            ),
            actual = client.getStudentGroups()
        )
        server.takeRequest(::assertGetStudentGroups)
    }

    @Test
    fun `getEmployees throws on non-200 code`() = runTest {
        server.enqueue { setResponseCode(404) }
        assertFailsWith<IllegalStateException> { client.getEmployees() }
        server.takeRequest(::assertGetEmployees)
    }

    @Test
    fun `getEmployees returns a list of employees`() = runTest {
        server.enqueue {
            setResponseCode(200)
            setBody(read("getEmployees.json"))
        }
        assertEquals(
            expected = listOf(
                Employee(
                    id = 505998,
                    urlId = "t-bobrova",
                    lastName = "Боброва",
                    firstName = "Татьяна",
                    middleName = "Сергеевна",
                    degree = "",
                    rank = null,
                    photoLink = "https://iis.bsuir.by/api/v1/employees/photo/505998"
                ),
                Employee(
                    id = 500089,
                    urlId = "v-bondarik",
                    lastName = "Бондарик",
                    firstName = "Василий",
                    middleName = "Михайлович",
                    degree = "к.т.н.",
                    rank = "доцент",
                    photoLink = "https://iis.bsuir.by/api/v1/employees/photo/500089"
                )
            ),
            actual = client.getEmployees()
        )
        server.takeRequest(::assertGetEmployees)
    }

    private fun read(fileName: String) = FileSystem.RESOURCES.read(fileName.toPath()) { readUtf8() }

    private fun assertGetStudentGroups(request: RecordedRequest) {
        assertEquals("GET", request.method)
        assertEquals(
            expected = HttpUrl(url) {
                addPathSegment("student-groups")
            },
            actual = request.requestUrl
        )
    }

    private fun assertGetEmployees(request: RecordedRequest) {
        assertEquals("GET", request.method)
        assertEquals(
            expected = HttpUrl(url) {
                addPathSegment("employees")
                addPathSegment("all")
            },
            actual = request.requestUrl
        )
    }
}
