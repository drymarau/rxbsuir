package by.toggi.rxbsuir.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.content.IntentCompat;

import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.activity.ScheduleActivity;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        Preference preference = findPreference(getString(R.string.key_pref_dark_theme));
        preference.setOnPreferenceChangeListener(this);
        boolean isDarkTheme = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean(preference.getKey(), false);
        preference.setSummary(isDarkTheme
                        ? R.string.summary_pref_dark_theme_enabled
                        : R.string.summary_pref_dark_theme_disabled
        );
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.getKey().equals(getString(R.string.key_pref_dark_theme))) {
            getActivity().finish();
            ComponentName componentName = new ComponentName(getActivity(), ScheduleActivity.class);
            final Intent intent = IntentCompat.makeMainActivity(componentName);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        return true;
    }
}
