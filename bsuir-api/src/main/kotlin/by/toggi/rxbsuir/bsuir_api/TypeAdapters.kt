package by.toggi.rxbsuir.bsuir_api

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter

object DayOfWeekAdapter {

  private const val MONDAY = "Понедельник"
  private const val TUESDAY = "Вторник"
  private const val WEDNESDAY = "Среда"
  private const val THURSDAY = "Четверг"
  private const val FRIDAY = "Пятница"
  private const val SATURDAY = "Суббота"
  private const val SUNDAY = "Воскресенье"

  @ToJson
  fun toJson(dayOfWeek: DayOfWeek): String {
    return when (dayOfWeek) {
      DayOfWeek.MONDAY -> MONDAY
      DayOfWeek.TUESDAY -> TUESDAY
      DayOfWeek.WEDNESDAY -> WEDNESDAY
      DayOfWeek.THURSDAY -> THURSDAY
      DayOfWeek.FRIDAY -> FRIDAY
      DayOfWeek.SATURDAY -> SATURDAY
      DayOfWeek.SUNDAY -> SUNDAY
    }
  }

  @FromJson
  fun fromJson(json: String): DayOfWeek {
    return when (json) {
      MONDAY -> DayOfWeek.MONDAY
      TUESDAY -> DayOfWeek.TUESDAY
      WEDNESDAY -> DayOfWeek.WEDNESDAY
      THURSDAY -> DayOfWeek.THURSDAY
      FRIDAY -> DayOfWeek.FRIDAY
      SATURDAY -> DayOfWeek.SATURDAY
      SUNDAY -> DayOfWeek.SUNDAY
      else -> throw IllegalArgumentException("$json is not day of week.")
    }
  }
}

object LessonTypeAdapter {

  private const val EXAM = "Экзамен"
  private const val LECTURE = "ЛК"
  private const val WORKSHOP = "ПЗ"
  private const val LAB = "ЛР"

  @ToJson
  fun toJson(type: LessonType): String {
    return when (type) {
      LessonType.EXAM -> EXAM
      LessonType.LECTURE -> LECTURE
      LessonType.WORKSHOP -> WORKSHOP
      LessonType.LAB -> LAB
      LessonType.UNKNOWN -> ""
    }
  }

  @FromJson
  fun fromJson(json: String): LessonType {
    return when (json) {
      EXAM -> LessonType.EXAM
      LECTURE -> LessonType.LECTURE
      WORKSHOP -> LessonType.WORKSHOP
      LAB -> LessonType.LAB
      else -> LessonType.UNKNOWN
    }
  }
}

object LocalTimeAdapter {

  @ToJson
  fun toJson(time: LocalTime): String {
    return time.toString()
  }

  @FromJson
  fun fromJson(json: String): LocalTime {
    return LocalTime.parse(json)
  }
}

object LocalDateAdapter {

  private val FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy")

  @ToJson
  fun toJson(date: LocalDate): String {
    return date.format(FORMATTER)
  }

  @FromJson
  fun fromJson(json: String): LocalDate {
    return LocalDate.parse(json, FORMATTER)
  }
}
