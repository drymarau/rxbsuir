package by.toggi.rxbsuir.api

/**
 * BSUIR REST API client.
 */
public interface BsuirClient {

    /**
     * Requests a list of student groups.
     *
     * @return a list of student groups
     */
    public suspend fun getStudentGroups(): List<StudentGroup>

    /**
     * Requests a list of employees
     *
     * @return a list of employees
     */
    public suspend fun getEmployees(): List<Employee>

    /**
     * Requests a list of announcements for an employee
     *
     * @return a list of announcements
     */
    public suspend fun getAnnouncements(urlId: String): List<Announcement>
}
