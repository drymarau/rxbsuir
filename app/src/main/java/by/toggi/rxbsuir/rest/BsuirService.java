package by.toggi.rxbsuir.rest;

import by.toggi.rxbsuir.rest.model.Employee;
import by.toggi.rxbsuir.rest.model.ScheduleJsonModels;
import by.toggi.rxbsuir.rest.model.StudentGroup;
import java.util.List;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

public interface BsuirService {

    @GET("/studentGroup/schedule")
    Observable<ScheduleJsonModels> getGroupSchedule(@Query("id") String group);

    @GET("/portal/employeeSchedule")
    Observable<ScheduleJsonModels> getEmployeeSchedule(@Query("employeeId") String employeeId);

    @GET("/groups")
    Observable<List<StudentGroup>> getStudentGroups();

    @GET("/employees")
    Observable<List<Employee>> getEmployees();

}
