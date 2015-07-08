package by.toggi.rxbsuir.rest;

import by.toggi.rxbsuir.model.ScheduleXmlModels;
import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

public interface BsuirService {

    @GET("/schedule/{group}")
    void getGroupSchedule(@Path("group") int group, Callback<ScheduleXmlModels> callback);

}
