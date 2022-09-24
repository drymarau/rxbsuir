package by.toggi.rxbsuir.api

import kotlinx.serialization.Serializable

/**
 * A BSUIR student group.
 *
 * @property id a unique ID of this student group
 * @property name a name of this student group
 * @property course a course of this student group
 */
@Serializable
public data class StudentGroup(
    val id: Long,
    val name: String,
    val course: Int?
)
