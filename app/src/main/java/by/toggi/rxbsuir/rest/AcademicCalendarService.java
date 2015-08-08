package by.toggi.rxbsuir.rest;

import by.toggi.rxbsuir.rest.model.AcademicCalendarResponse;
import retrofit.http.GET;
import rx.Observable;

public interface AcademicCalendarService {

    @GET("/list")
    Observable<AcademicCalendarResponse> getAcademicCalendar();

}
