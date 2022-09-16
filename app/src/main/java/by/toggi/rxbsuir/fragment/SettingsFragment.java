package by.toggi.rxbsuir.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import by.toggi.rxbsuir.BuildConfig;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.Utils;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingsFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceClickListener {

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        findPreference("build_version").setSummary(BuildConfig.VERSION_NAME);
        findPreference("rate_app").setOnPreferenceClickListener(this);
        findPreference("privacy_policy").setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        switch (preference.getKey()) {
            case "rate_app":
                Utils.openPlayStorePage(getActivity());
                return true;
            case "privacy_policy":
                Utils.openPrivacyPolicyPage(getActivity());
                return true;
        }
        return false;
    }
}
