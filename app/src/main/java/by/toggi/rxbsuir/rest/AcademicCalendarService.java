package by.toggi.rxbsuir.rest;

import android.database.Observable;

import by.toggi.rxbsuir.rest.model.AcademicCalendarResponse;
import retrofit.http.GET;

public interface AcademicCalendarService {

    @GET("/list")
    Observable<AcademicCalendarResponse> getAcademicCalendar();

}
