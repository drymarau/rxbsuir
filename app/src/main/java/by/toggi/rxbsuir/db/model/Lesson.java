package by.toggi.rxbsuir.db.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.format.TextStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import by.toggi.rxbsuir.rest.model.Employee;
import by.toggi.rxbsuir.rest.model.Schedule;

public class Lesson {

    private Long _id;
    private String syncId;
    private List<String> auditoryList;
    private List<Employee> employeeList;
    private String lessonTime;
    private String lessonType;
    private String note;
    private int numSubgroup;
    private List<String> studentGroupList;
    private String subject;
    private List<Integer> weekNumberList;
    private DayOfWeek weekday;
    private boolean isGroupSchedule;

    public Lesson(@Nullable Long _id, @NonNull String syncId, List<String> auditoryList, List<Employee> employeeList, String lessonTime, String lessonType, String note, int numSubgroup, List<String> studentGroupList, String subject, List<Integer> weekNumberList, DayOfWeek weekday, boolean isGroupSchedule) {
        this._id = _id;
        this.syncId = syncId;
        this.auditoryList = auditoryList;
        this.employeeList = employeeList;
        this.lessonTime = lessonTime;
        this.lessonType = lessonType;
        this.note = note;
        this.numSubgroup = numSubgroup;
        this.studentGroupList = studentGroupList;
        this.subject = subject;
        this.weekNumberList = weekNumberList;
        this.weekday = weekday;
        this.isGroupSchedule = isGroupSchedule;
    }

    public Lesson(@Nullable Long _id, @NonNull String syncId, Schedule schedule, DayOfWeek weekday, boolean isGroupSchedule) {
        this._id = _id;
        this.syncId = syncId;
        this.auditoryList = schedule.auditory;
        this.employeeList = schedule.employeeList;
        this.lessonTime = schedule.lessonTime;
        this.lessonType = schedule.lessonType;
        this.note = schedule.note;
        this.numSubgroup = schedule.numSubgroup;
        this.studentGroupList = schedule.studentGroupList;
        this.subject = schedule.subject;
        this.weekNumberList = schedule.weekNumberList;
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

    public String getLessonTime() {
        return lessonTime;
    }

    /**
     * Gets pretty lesson time (two-line time).
     *
     * @return the pretty lesson time
     */
    public String getPrettyLessonTime() {
        return lessonTime.replace("-", "\n");
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

    public String getPrettyWeekday() {
        String prettyWeekday = weekday.getDisplayName(TextStyle.FULL, Locale.getDefault());
        return prettyWeekday.substring(0, 1).toUpperCase() + prettyWeekday.substring(1);
    }

    public boolean isGroupSchedule() {
        return isGroupSchedule;
    }
}
