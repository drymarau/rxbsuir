package by.toggi.rxbsuir.db.model;

import android.support.annotation.Nullable;

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

    public List<Employee> getEmployeeList() {
        return employeeList;
    }

    public String getLessonTime() {
        return lessonTime;
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

    public List<Integer> getWeekNumberList() {
        return weekNumberList;
    }

    public String getWeekday() {
        return weekday;
    }
}
