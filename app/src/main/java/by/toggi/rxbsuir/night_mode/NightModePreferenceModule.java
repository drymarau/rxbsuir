package by.toggi.rxbsuir.night_mode;

import androidx.appcompat.app.AppCompatDelegate;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;

import javax.inject.Named;

import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.dagger.PerApp;
import dagger.Module;
import dagger.Provides;

@Module public class NightModePreferenceModule {

  @Provides @PerApp @Named(PreferenceHelper.NIGHT_MODE) Preference<String> provide(
      RxSharedPreferences prefs) {
    return prefs.getString(PreferenceHelper.NIGHT_MODE,
        String.valueOf(AppCompatDelegate.MODE_NIGHT_NO));
  }
}
