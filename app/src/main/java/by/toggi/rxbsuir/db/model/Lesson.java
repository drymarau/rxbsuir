package by.toggi.rxbsuir.db.model;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.List;

import by.toggi.rxbsuir.rest.model.Employee;
import by.toggi.rxbsuir.rest.model.Schedule;

public class Lesson {

    private Long _id;
    private List<String> auditoryList;
    private List<Employee> employeeList;
    private String lessonTime;
    private String lessonType;
    private String note;
    private int numSubgroup;
    private List<String> studentGroupList;
    private String subject;
    private List<Integer> weekNumberList;
    private String weekday;

    public Lesson(@Nullable Long _id, List<String> auditoryList, List<Employee> employeeList, String lessonTime, String lessonType, String note, int numSubgroup, List<String> studentGroupList, String subject, List<Integer> weekNumberList, String weekday) {
        this._id = _id;
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
    }

    public Lesson(@Nullable Long _id, Schedule schedule, String weekday) {
        this._id = _id;
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
    }

    public Long getId() {
        return _id;
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
            return TextUtils.join(", ", employeeList);
        } else {
            return "";
        }
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

    public String getSubject() {
        return subject;
    }

    /**
     * Gets subject with subgroup ("ЭМАСиК (1)").
     *
     * @return the subject with subgroup
     */
    public String getSubjectWithSubgroup() {
        if (lessonType.equals("ЛР")) {
            return String.format("%s (%d)", subject, numSubgroup);
        } else {
            return subject;
        }
    }

    public List<Integer> getWeekNumberList() {
        return weekNumberList;
    }

    public String getWeekday() {
        return weekday;
    }
}
