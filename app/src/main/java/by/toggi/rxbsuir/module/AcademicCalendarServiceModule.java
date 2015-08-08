package by.toggi.rxbsuir.module;

import javax.inject.Named;
import javax.inject.Singleton;

import by.toggi.rxbsuir.rest.AcademicCalendarService;
import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

@Module
public class AcademicCalendarServiceModule {

    private final String mEndpoint;

    public AcademicCalendarServiceModule(String endpoint) {
        mEndpoint = endpoint;
    }

    @Provides
    @Singleton
    @Named(value = "gson")
    RestAdapter provideRestAdapter() {
        return new RestAdapter.Builder()
                .setEndpoint(mEndpoint)
                .build();
    }

    @Provides
    @Singleton
    AcademicCalendarService provideAcademicCalendarService(@Named("gson") RestAdapter restAdapter) {
        return restAdapter.create(AcademicCalendarService.class);
    }

}
