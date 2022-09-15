package by.toggi.rxbsuir.di;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class GsonModule {

    @Provides
    @Singleton
    Gson provide() {
        return new Gson();
    }
}
