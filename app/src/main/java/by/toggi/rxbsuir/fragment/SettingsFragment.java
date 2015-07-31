package by.toggi.rxbsuir.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.content.IntentCompat;

import javax.inject.Inject;
import javax.inject.Named;

import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.activity.ScheduleActivity;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    @Inject @Named(ScheduleActivity.KEY_IS_DARK_THEME) boolean mIsDarkTheme;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((RxBsuirApplication) getActivity().getApplication()).getAppComponent().inject(this);

        addPreferencesFromResource(R.xml.preferences);

        Preference preference = findPreference(ScheduleActivity.KEY_IS_DARK_THEME);
        preference.setOnPreferenceChangeListener(this);
        preference.setSummary(mIsDarkTheme
                        ? R.string.summary_pref_dark_theme_enabled
                        : R.string.summary_pref_dark_theme_disabled
        );
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.getKey().equals(ScheduleActivity.KEY_IS_DARK_THEME)) {
            getActivity().finish();
            ComponentName componentName = new ComponentName(getActivity(), ScheduleActivity.class);
            final Intent intent = IntentCompat.makeMainActivity(componentName);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        return true;
    }
}
