package by.toggi.rxbsuir.rest.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * The type Academic calendar response.
 */
public class AcademicCalendarResponse {

    @SerializedName(value = "items")
    private List<AcademicCalendar> academicCalendarList;

    /**
     * Instantiates a new Academic calendar response.
     */
    public AcademicCalendarResponse() {
    }

    /**
     * Gets academic calendar list.
     *
     * @return the academic calendar list
     */
    public List<AcademicCalendar> getAcademicCalendarList() {
        return academicCalendarList;
    }
}
