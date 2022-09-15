package by.toggi.rxbsuir;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

import com.f2prateek.rx.preferences.Preference;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.hilt.android.HiltAndroidApp;
import timber.log.Timber;

@HiltAndroidApp
public class RxBsuirApplication extends Application {

    @Inject
    Timber.Tree mTree;
    @Inject
    @Named(PreferenceHelper.NIGHT_MODE)
    Preference<String> mNightModePreference;

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(mTree);

        mNightModePreference.asObservable()
                .map(Integer::valueOf)
                .onErrorReturn(throwable -> AppCompatDelegate.MODE_NIGHT_NO)
                .subscribe(AppCompatDelegate::setDefaultNightMode);
    }
}
