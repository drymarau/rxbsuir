package by.toggi.rxbsuir.module;

import javax.inject.Singleton;

import by.toggi.rxbsuir.rest.BsuirService;
import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.SimpleXMLConverter;

@Module
public class BsuirServiceModule {

    private final String mEndpoint;

    public BsuirServiceModule(String endpoint) {
        mEndpoint = endpoint;
    }

    @Provides
    @Singleton
    BsuirService provideBsuirService() {
        return new RestAdapter.Builder()
                .setConverter(new SimpleXMLConverter())
                .setEndpoint(mEndpoint)
                .build()
                .create(BsuirService.class);
    }

}
