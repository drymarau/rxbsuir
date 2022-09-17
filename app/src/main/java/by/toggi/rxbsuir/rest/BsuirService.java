package by.toggi.rxbsuir.rest;

import java.util.List;

import by.toggi.rxbsuir.rest.model.Employee;
import by.toggi.rxbsuir.rest.model.ScheduleJsonModels;
import by.toggi.rxbsuir.rest.model.StudentGroup;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BsuirService {

    @GET("api/v1/studentGroup/schedule")
    Response<ScheduleJsonModels> getGroupSchedule(@Query("id") String group);

    @GET("api/v1/portal/employeeSchedule")
    Response<ScheduleJsonModels> getEmployeeSchedule(@Query("employeeId") String employeeId);

    @GET("api/v1/groups")
    Response<List<StudentGroup>> getStudentGroups();

    @GET("api/v1/employees")
    Response<List<Employee>> getEmployees();
}
