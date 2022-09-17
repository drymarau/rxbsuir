package by.toggi.rxbsuir;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;

import javax.inject.Inject;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class RxBsuirApplication extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Inject
    SharedPreferences mSharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (getString(R.string.night_mode_key).equals(key)) {
            var nightModeString = sharedPreferences.getString(key, getString(R.string.night_mode_default_value));
            var nightMode = Integer.parseInt(nightModeString);
            AppCompatDelegate.setDefaultNightMode(nightMode);
        }
    }
}
