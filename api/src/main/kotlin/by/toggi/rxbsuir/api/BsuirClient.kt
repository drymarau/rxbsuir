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
}
