package by.toggi.rxbsuir.night_mode;

import android.support.v7.app.AppCompatDelegate;
import by.toggi.rxbsuir.PreferenceHelper;
import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;

@Module public class NightModePreferenceModule {

  @Provides
  @Named(PreferenceHelper.NIGHT_MODE)
  Preference<String> provide(RxSharedPreferences prefs) {
    return prefs.getString(PreferenceHelper.NIGHT_MODE, String.valueOf(AppCompatDelegate.MODE_NIGHT_NO));
  }
}
