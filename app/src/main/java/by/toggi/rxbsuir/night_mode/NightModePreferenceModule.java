package by.toggi.rxbsuir.night_mode;

import androidx.appcompat.app.AppCompatDelegate;

import com.f2prateek.rx.preferences.Preference;
import com.f2prateek.rx.preferences.RxSharedPreferences;

import javax.inject.Named;
import javax.inject.Singleton;

import by.toggi.rxbsuir.PreferenceHelper;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class NightModePreferenceModule {

    @Provides
    @Singleton
    @Named(PreferenceHelper.NIGHT_MODE)
    Preference<String> provide(RxSharedPreferences prefs) {
        return prefs.getString(PreferenceHelper.NIGHT_MODE, String.valueOf(AppCompatDelegate.MODE_NIGHT_NO));
    }
}
