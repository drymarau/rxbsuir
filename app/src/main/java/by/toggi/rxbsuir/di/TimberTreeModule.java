package by.toggi.rxbsuir.di;

import javax.inject.Singleton;

import by.toggi.rxbsuir.BuildConfig;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import timber.log.Timber;

@Module
@InstallIn(SingletonComponent.class)
public class TimberTreeModule {

    @Provides
    @Singleton
    Timber.Tree provide() {
        Timber.Tree tree;
        if (BuildConfig.DEBUG) {
            tree = new Timber.DebugTree();
        } else {
            tree = new CrashReportingTree();
        }
        return tree;
    }

    private static class CrashReportingTree extends Timber.Tree {

        @Override
        protected void log(int priority, String tag, String message, Throwable t) {
        }
    }
}
