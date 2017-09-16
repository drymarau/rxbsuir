package by.toggi.rxbsuir.dagger.module;

import android.util.Log;
import by.toggi.rxbsuir.dagger.PerApp;
import com.crashlytics.android.Crashlytics;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import timber.log.Timber;

@Module public class TimberTreeModule {

  @Provides @PerApp Timber.Tree provide(@Named("debug") boolean debug) {
    Timber.Tree tree;
    if (debug) {
      tree = new Timber.DebugTree();
    } else {
      tree = new CrashReportingTree();
    }
    return tree;
  }

  private static class CrashReportingTree extends Timber.Tree {

    @Override protected void log(int priority, String tag, String message, Throwable t) {
      if (priority == Log.VERBOSE || priority == Log.DEBUG) {
        return;
      }

      Crashlytics.log(priority, tag, message);
      if (t != null) {
        Crashlytics.logException(t);
      }
    }
  }
}
