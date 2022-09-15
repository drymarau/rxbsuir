package by.toggi.rxbsuir.di;

import android.app.Application;

import java.io.File;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.Cache;

@Module
@InstallIn(SingletonComponent.class)
public class OkHttpCacheModule {

    private static final String NAME = "responses";
    private static final long SIZE = 5 * 1024 * 1024;

    @Provides
    @Singleton
    Cache provide(Application application) {
        File directory = new File(application.getCacheDir(), NAME);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        return new Cache(directory, SIZE);
    }
}
