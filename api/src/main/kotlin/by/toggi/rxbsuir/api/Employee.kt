package by.toggi.rxbsuir.api

import kotlinx.serialization.Serializable

/**
 * A BSUIR employee.
 *
 * @property urlId a unique URL id to query other resources
 * @property lastName a last name of this employee
 * @property firstName a first name of this employee
 * @property middleName a middle name of this employee
 * @property photoLink a URL which contains a photo of this employee
 * @property degree a scientific degree, or an empty string if none
 * @property rank a rank of this employee
 */
@Serializable
public data class Employee(
    val urlId: String,
    val lastName: String,
    val firstName: String,
    val middleName: String,
    val photoLink: String,
    val degree: String,
    val rank: String?,
)
