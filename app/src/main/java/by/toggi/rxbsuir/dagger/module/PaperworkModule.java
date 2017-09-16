package by.toggi.rxbsuir.dagger.module;

import android.content.Context;
import by.toggi.rxbsuir.dagger.PerApp;
import dagger.Module;
import dagger.Provides;
import hu.supercluster.paperwork.Paperwork;

@Module public class PaperworkModule {

  @Provides @PerApp Paperwork provide(Context context) {
    return new Paperwork(context);
  }
}
