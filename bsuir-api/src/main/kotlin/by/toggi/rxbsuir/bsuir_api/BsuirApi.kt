package by.toggi.rxbsuir.bsuir_api

import com.squareup.moshi.Json
import io.reactivex.Single
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import retrofit2.http.GET
import retrofit2.http.Query

interface BsuirService {

  @GET("api/v1/groups")
  fun groups(): Single<List<GroupResponse>>

  @GET("api/v1/employees")
  fun employees(): Single<List<EmployeeResponse>>

  @GET("api/v1/studentGroup/schedule")
  fun groupSchedules(
      @Query("id") id: Long
  ): Single<GroupScheduleResponse>

  @GET("api/v1/portal/employeeSchedule")
  fun employeeSchedules(
      @Query("employeeId") id: Long
  ): Single<EmployeeScheduleResponse>

  @GET("api/v1/studentGroup/schedule")
  fun groupExamSchedules(
      @Query("id") id: Long
  ): Single<GroupExamScheduleResponse>

  @GET("api/v1/portal/employeeSchedule")
  fun employeeExamSchedules(
      @Query("employeeId") id: Long
  ): Single<EmployeeExamScheduleResponse>
}

data class GroupResponse(
    val id: Long,
    val name: String,
    val course: Int?,
    val facultyId: Long,
    val facultyName: String?,
    @Json(name = "specialityDepartmentEducationFormId") val specialityId: Long,
    val specialityName: String?,
    @Json(name = "calendarId") val calendarUri: String
)

data class EmployeeResponse(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val middleName: String?,
    @Json(name = "fio") val displayName: String,
    @Json(name = "academicDepartment") val departments: List<String>,
    val rank: String?,
    @Json(name = "photoLink") val photoUrl: String?,
    @Json(name = "calendarId") val calendarUri: String
)

data class GroupScheduleResponse(
    @Json(name = "studentGroup") val group: GroupResponse,
    val schedules: List<ScheduleResponse>
)

data class EmployeeScheduleResponse(
    val employee: EmployeeResponse,
    val schedules: List<ScheduleResponse>
)

data class GroupExamScheduleResponse(
    @Json(name = "studentGroup") val group: GroupResponse,
    val examSchedules: List<ExamScheduleResponse>
)

data class EmployeeExamScheduleResponse(
    val employee: EmployeeResponse,
    val examSchedules: List<ExamScheduleResponse>
)

data class ScheduleResponse(
    @Json(name = "weekDay") val dayOfWeek: DayOfWeek,
    @Json(name = "schedule") val lessons: List<LessonResponse>
)

data class ExamScheduleResponse(
    @Json(name = "weekDay") val date: LocalDate,
    @Json(name = "schedule") val exams: List<ExamResponse>
)

data class LessonResponse(
    val subject: String,
    @Json(name = "lessonType") val type: LessonType,
    @Json(name = "startLessonTime") val start: LocalTime,
    @Json(name = "endLessonTime") val end: LocalTime,
    val weekNumber: List<Int>,
    @Json(name = "numSubgroup") val subgroup: Int,
    @Json(name = "studentGroup") val groups: List<String>,
    @Json(name = "auditory") val auditories: List<String>,
    val note: String,
    val employee: List<EmployeeResponse>,
    @Json(name = "zaoch") val extramural: Boolean = false
)

data class ExamResponse(
    val subject: String,
    @Json(name = "lessonType") val type: LessonType,
    @Json(name = "startLessonTime") val start: LocalTime,
    @Json(name = "endLessonTime") val end: LocalTime,
    @Json(name = "numSubgroup") val subgroup: Int,
    @Json(name = "studentGroup") val groups: List<String>,
    @Json(name = "auditory") val auditories: List<String>,
    val note: String,
    val employee: List<EmployeeResponse>,
    @Json(name = "zaoch") val extramural: Boolean = false
)

enum class LessonType {

  EXAM,
  LECTURE,
  WORKSHOP,
  LAB,
  UNKNOWN
}
