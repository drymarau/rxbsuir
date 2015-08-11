package by.toggi.rxbsuir.module;

import javax.inject.Named;
import javax.inject.Singleton;

import by.toggi.rxbsuir.rest.BsuirService;
import dagger.Module;
import dagger.Provides;
import retrofit.RestAdapter;
import retrofit.converter.Converter;
import retrofit.converter.SimpleXMLConverter;

@Module
public class BsuirServiceModule {

    private final String mEndpoint;

    public BsuirServiceModule(String endpoint) {
        mEndpoint = endpoint;
    }

    @Provides
    @Singleton
    Converter provideConverter() {
        return new SimpleXMLConverter();
    }

    @Provides
    @Singleton
    @Named("simple")
    RestAdapter provideRestAdapter(Converter converter) {
        return new RestAdapter.Builder()
                .setConverter(converter)
                .setEndpoint(mEndpoint)
                .build();
    }

    @Provides
    @Singleton
    BsuirService provideBsuirService(@Named("simple") RestAdapter restAdapter) {
        return restAdapter.create(BsuirService.class);
    }

}
