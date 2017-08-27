package by.toggi.rxbsuir.dagger.module;

import javax.inject.Singleton;

import by.toggi.rxbsuir.rest.BsuirService;
import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;

@Module
public class BsuirServiceModule {

    private final String mEndpoint;

    public BsuirServiceModule(String endpoint) {
        mEndpoint = endpoint;
    }

    @Provides
    @Singleton
    RestAdapter provideRestAdapter() {
        return new RestAdapter.Builder().setEndpoint(mEndpoint).build();
    }

    @Provides
    @Singleton
    BsuirService provideBsuirService(RestAdapter restAdapter) {
        return restAdapter.create(BsuirService.class);
    }

}
