package by.toggi.rxbsuir.api

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
public data class Announcement(
    val id: Long,
    val content: String,
    @Contextual val date: LocalDate,
    val startTime: LocalTime,
    val endTime: LocalTime,
)
