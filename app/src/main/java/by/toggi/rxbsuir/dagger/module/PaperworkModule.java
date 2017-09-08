package by.toggi.rxbsuir.dagger.module;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import hu.supercluster.paperwork.Paperwork;

@Module public class PaperworkModule {

  @Provides Paperwork provide(Context context) {
    return new Paperwork(context);
  }
}
