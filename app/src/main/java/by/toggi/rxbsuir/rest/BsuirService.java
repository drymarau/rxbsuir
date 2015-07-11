package by.toggi.rxbsuir.rest;

import by.toggi.rxbsuir.model.ScheduleXmlModels;
import retrofit.http.GET;
import retrofit.http.Path;
import rx.Observable;

public interface BsuirService {

    @GET("/schedule/{group}")
    Observable<ScheduleXmlModels> getGroupSchedule(@Path("group") int group);

}
