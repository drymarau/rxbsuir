package by.toggi.rxbsuir.rest.model;

import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

import static by.toggi.rxbsuir.db.RxBsuirContract.AcademicCalendarEntry;

/**
 * The type Academic calendar.
 */
@StorIOSQLiteType(table = AcademicCalendarEntry.TABLE_NAME)
public class AcademicCalendar {

    @StorIOSQLiteColumn(name = AcademicCalendarEntry.COL_COURSE, key = true)
    public int course;
    @StorIOSQLiteColumn(name = AcademicCalendarEntry.COL_START_DATE)
    public String startDate;
    @StorIOSQLiteColumn(name = AcademicCalendarEntry.COL_END_DATE)
    public String endDate;

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
