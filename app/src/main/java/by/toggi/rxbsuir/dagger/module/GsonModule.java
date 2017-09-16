package by.toggi.rxbsuir.dagger.module;

import by.toggi.rxbsuir.dagger.PerApp;
import com.google.gson.Gson;
import dagger.Module;
import dagger.Provides;

@Module public class GsonModule {

  @Provides @PerApp Gson provide() {
    return new Gson();
  }
}
