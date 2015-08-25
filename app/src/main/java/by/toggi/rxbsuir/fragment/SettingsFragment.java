package by.toggi.rxbsuir.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import by.toggi.rxbsuir.BuildConfig;
import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.Utils;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        findPreference(PreferenceHelper.IS_DARK_THEME).setOnPreferenceChangeListener(this);
        findPreference("build_version").setSummary(BuildConfig.VERSION_NAME);
        findPreference("rate_app").setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference.getKey().equals(PreferenceHelper.IS_DARK_THEME)) {
            Utils.restartApp(getActivity());
        }
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case "rate_app":
                try {
                    startActivity(new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)
                    ));
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getActivity(), R.string.play_store_not_found, Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return false;
    }
}
