package by.toggi.rxbsuir.db.model;

import java.util.List;

import by.toggi.rxbsuir.rest.model.Employee;
import by.toggi.rxbsuir.rest.model.Schedule;

public class Lesson {

    private List<String> auditoryList;
    private List<Employee> employeeList;
    private String lessonTime;
    private String lessonType;
    private String note;
    private int numSubgroup;
    private List<Integer> studentGroupList;
    private String subject;
    private List<Integer> weekNumberList;
    private String weekday;

    public Lesson(List<String> auditoryList, List<Employee> employeeList, String lessonTime, String lessonType, String note, int numSubgroup, List<Integer> studentGroupList, String subject, List<Integer> weekNumberList, String weekday) {
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

    public Lesson(Schedule schedule, String weekday) {
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

    public List<Integer> getStudentGroupList() {
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
