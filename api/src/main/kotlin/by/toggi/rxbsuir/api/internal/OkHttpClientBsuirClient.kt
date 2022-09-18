package by.toggi.rxbsuir.api.internal

import by.toggi.rxbsuir.api.BsuirClient
import by.toggi.rxbsuir.api.Employee
import by.toggi.rxbsuir.api.StudentGroup
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.okio.decodeFromBufferedSource
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class OkHttpClientBsuirClient(
    private val client: OkHttpClient,
    private val json: Json,
    private val url: HttpUrl
) : BsuirClient {

    @Inject
    constructor(client: OkHttpClient) : this(
        client = client,
        json = Json {
            ignoreUnknownKeys = true
        },
        url = HttpUrl {
            scheme("https")
            host("iis.bsuir.by")
            addPathSegment("api")
            addPathSegment("v1")
        }
    )

    override suspend fun getStudentGroups(): List<StudentGroup> = client
        .newCall {
            get()
            url(url) {
                addPathSegment("student-groups")
            }
            accept(ApplicationJson)
        }
        .await(::handleGetStudentGroupsResponse)

    override suspend fun getEmployees(): List<Employee> = client
        .newCall {
            get()
            url(url) {
                addPathSegment("employees")
                addPathSegment("all")
            }
            accept(ApplicationJson)
        }
        .await(::handleGetEmployeesResponse)

    @OptIn(ExperimentalSerializationApi::class)
    private fun handleGetStudentGroupsResponse(response: Response): List<StudentGroup> =
        when (response.code) {
            200 -> json.decodeFromBufferedSource(response.body!!.source())
            else -> throw IllegalStateException()
        }

    @OptIn(ExperimentalSerializationApi::class)
    private fun handleGetEmployeesResponse(response: Response): List<Employee> =
        when (response.code) {
            200 -> json.decodeFromBufferedSource(response.body!!.source())
            else -> throw IllegalStateException()
        }
}
