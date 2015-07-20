package by.toggi.rxbsuir.rest;

import by.toggi.rxbsuir.rest.model.ScheduleXmlModels;
import by.toggi.rxbsuir.rest.model.StudentGroupXmlModels;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface BsuirService {

    @GET("/schedule/{group}")
    Observable<ScheduleXmlModels> getGroupSchedule(@Path("group") String group);

    @GET("/studentGroup")
    Observable<StudentGroupXmlModels> getStudentGroups();

}
