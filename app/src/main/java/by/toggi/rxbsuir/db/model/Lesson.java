package by.toggi.rxbsuir.db.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.parceler.Parcel;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import by.toggi.rxbsuir.rest.model.Employee;
import by.toggi.rxbsuir.rest.model.Schedule;
import timber.log.Timber;

@Parcel
public class Lesson {

    Long _id;
    String syncId;
    List<String> auditoryList;
    List<Employee> employeeList;
    LocalTime lessonTimeStart;
    LocalTime lessonTimeEnd;
    String lessonType;
    String note;
    int numSubgroup;
    List<String> studentGroupList;
    String subject;
    List<Integer> weekNumberList;
    DayOfWeek weekday;
    boolean isGroupSchedule;

    public Lesson() {
    }

    public Lesson(@Nullable Long _id, @NonNull String syncId, List<String> auditoryList, List<Employee> employeeList, LocalTime lessonTimeStart, LocalTime lessonTimeEnd, String lessonType, String note, int numSubgroup, List<String> studentGroupList, String subject, List<Integer> weekNumberList, DayOfWeek weekday, boolean isGroupSchedule) {
        this._id = _id;
        this.syncId = syncId;
        this.auditoryList = auditoryList;
        this.employeeList = employeeList;
        this.lessonTimeStart = lessonTimeStart;
        this.lessonTimeEnd = lessonTimeEnd;
        this.lessonType = lessonType;
        this.note = note;
        this.numSubgroup = numSubgroup;
        this.studentGroupList = studentGroupList;
        this.subject = subject;
        this.weekNumberList = weekNumberList;
        this.weekday = weekday;
        this.isGroupSchedule = isGroupSchedule;
    }

    public Lesson(@NonNull String syncId, Schedule schedule, DayOfWeek weekday, boolean isGroupSchedule) {
        this._id = null;
        this.syncId = syncId;
        this.auditoryList = schedule.auditory;
        this.employeeList = schedule.employee;
        String[] lessonTime = TextUtils.split(schedule.lessonTime, "-");
        try {
            this.lessonTimeStart = LocalTime.parse(lessonTime[0]);
            this.lessonTimeEnd = LocalTime.parse(lessonTime[1]);
        } catch (IndexOutOfBoundsException e) {
            Timber.e("Something went wrong: " + e.getMessage());
        }
        this.lessonType = schedule.lessonType;
        this.note = schedule.note;
        this.numSubgroup = schedule.numSubgroup;
        this.studentGroupList = schedule.studentGroup;
        this.subject = schedule.subject;
        this.weekNumberList = schedule.weekNumber;
        this.weekday = weekday;
        this.isGroupSchedule = isGroupSchedule;
    }

    public Long getId() {
        return _id;
    }

    public String getSyncId() {
        return syncId;
    }

    public List<String> getAuditoryList() {
        return auditoryList;
    }

    /**
     * Gets pretty auditory list ("122-1, 135-1").
     *
     * @return pretty auditory list
     */
    public String getPrettyAuditoryList() {
        if (auditoryList != null) {
            return TextUtils.join(", ", auditoryList);
        } else {
            return "";
        }
    }

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    /**
     * Gets pretty employee list ("Дик С.К., Смирнов А.В.").
     *
     * @return the pretty employee list
     */
    public String getPrettyEmployeeList() {
        if (employeeList != null) {
            List<String> employeeStringList = new ArrayList<>(employeeList.size());
            for (Employee employee : employeeList) {
                employeeStringList.add(employee.getShortFullName());
            }
            return TextUtils.join(", ", employeeStringList);
        }
        return "";
    }

    public LocalTime getLessonTimeStart() {
        return lessonTimeStart;
    }

    /**
     * Gets pretty lesson time start ("8:00").
     *
     * @return the pretty lesson time start
     */
    public String getPrettyLessonTimeStart() {
        return lessonTimeStart.toString();
    }

    public LocalTime getLessonTimeEnd() {
        return lessonTimeEnd;
    }

    /**
     * Gets pretty lesson time end ("9:45").
     *
     * @return the pretty lesson time end
     */
    public String getPrettyLessonTimeEnd() {
        return lessonTimeEnd.toString();
    }

    public String getLessonType() {
        return lessonType;
    }

    public String getNote() {
        return note;
    }

    public int getNumSubgroup() {
        return numSubgroup;
    }

    public List<String> getStudentGroupList() {
        return studentGroupList;
    }

    /**
     * Gets pretty student group list ("111801, 011801").
     *
     * @return the pretty student group list
     */
    public String getPrettyStudentGroupList() {
        return TextUtils.join(", ", studentGroupList);
    }

    public String getSubject() {
        return subject;
    }

    /**
     * Gets subject with subgroup ("ЭМАСиК (1)").
     *
     * @return the subject with subgroup
     */
    public String getSubjectWithSubgroup() {
        if (lessonType.equals("ЛР") || (lessonType.equals("ПЗ") && numSubgroup != 0)) {
            return String.format("%s (%d)", subject, numSubgroup);
        } else {
            return subject;
        }
    }

    public List<Integer> getWeekNumberList() {
        return weekNumberList;
    }

    public DayOfWeek getWeekday() {
        return weekday;
    }

    /**
     * Gets pretty weekday.
     *
     * @return the pretty weekday
     */
    public String getPrettyWeekday() {
        var prettyWeekday = weekday.getDisplayName(TextStyle.FULL, Locale.getDefault());
        return prettyWeekday.substring(0, 1).toUpperCase() + prettyWeekday.substring(1);
    }

    public boolean isGroupSchedule() {
        return isGroupSchedule;
    }

    public String getPrettyLesson() {
        if (getPrettyAuditoryList().isEmpty()) {
            return String.format(
                    "%s-%s %s (%s)",
                    getPrettyLessonTimeStart(),
                    getPrettyLessonTimeEnd(),
                    getSubjectWithSubgroup(),
                    getLessonType()
            );
        } else {
            return String.format(
                    "%s-%s %s (%s) @ %s",
                    getPrettyLessonTimeStart(),
                    getPrettyLessonTimeEnd(),
                    getSubjectWithSubgroup(),
                    getLessonType(),
                    getPrettyAuditoryList());
        }
    }

    public String getPrettyWeekNumberList() {
        if (weekNumberList != null && !weekNumberList.isEmpty()) {
            if (weekNumberList.contains(0)) {
                return null;
            } else {
                return TextUtils.join(", ", weekNumberList);
            }
        }
        return null;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
