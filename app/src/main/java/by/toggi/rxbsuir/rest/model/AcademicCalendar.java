package by.toggi.rxbsuir.rest.model;

/**
 * The type Academic calendar.
 */
public class AcademicCalendar {

    private int course;
    private String startDate;
    private String endDate;

    /**
     * Instantiates a new Academic calendar.
     */
    public AcademicCalendar() {
    }

    /**
     * Gets course.
     *
     * @return the course
     */
    public int getCourse() {
        return course;
    }

    /**
     * Gets start date.
     *
     * @return the start date
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * Gets end date.
     *
     * @return the end date
     */
    public String getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return "AcademicCalendar{" +
                "course=" + course +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                '}';
    }
}
