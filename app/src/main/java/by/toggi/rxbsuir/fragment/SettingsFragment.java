package by.toggi.rxbsuir.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.timepicker.MaterialTimePicker;

import java.time.LocalTime;

import javax.inject.Inject;
import javax.inject.Named;

import by.toggi.rxbsuir.BuildConfig;
import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.Utils;
import dagger.hilt.android.AndroidEntryPoint;
import rx.Subscription;

@AndroidEntryPoint
public class SettingsFragment extends PreferenceFragmentCompat
        implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    @Inject
    com.f2prateek.rx.preferences.Preference<LocalTime> mLocalTimePreference;
    @Inject
    @Named(PreferenceHelper.FAVORITE_SYNC_ID)
    com.f2prateek.rx.preferences.Preference<String> mFavoriteSyncIdPrerefence;

    private Subscription mSubscription;
    private Preference mNotificationTimePreference;

    @Override
    public void onCreatePreferences(@Nullable Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        findPreference("build_version").setSummary(BuildConfig.VERSION_NAME);
        findPreference("rate_app").setOnPreferenceClickListener(this);
        findPreference("privacy_policy").setOnPreferenceClickListener(this);
        findPreference(PreferenceHelper.IS_TODAY_ENABLED).setOnPreferenceChangeListener(this);
        findPreference(PreferenceHelper.ARE_CIRCLES_COLORED).setOnPreferenceChangeListener(this);
        findPreference(PreferenceHelper.IS_FAM_ENABLED).setOnPreferenceChangeListener(this);
        mNotificationTimePreference = findPreference("notification_time");
        mNotificationTimePreference.setEnabled(mFavoriteSyncIdPrerefence.get() != null);
        mNotificationTimePreference.setOnPreferenceClickListener(preference -> {
            var localTime = mLocalTimePreference.get();
            var picker = new MaterialTimePicker.Builder()
                    .setHour(localTime.getHour())
                    .setMinute(localTime.getMinute())
                    .build();
            picker.addOnPositiveButtonClickListener(v -> mLocalTimePreference.set(LocalTime.of(picker.getHour(), picker.getMinute())));
            picker.show(getChildFragmentManager(), null);
            return true;
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mSubscription = mLocalTimePreference.asObservable().subscribe(localTime -> {
            if (mNotificationTimePreference != null) {
                mNotificationTimePreference.setSummary(localTime.toString());
            }
            if (mFavoriteSyncIdPrerefence.get() != null) {
                Utils.setNotificationAlarm(getActivity(), localTime);
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        Utils.unsubscribe(mSubscription);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        switch (preference.getKey()) {
            case PreferenceHelper.IS_TODAY_ENABLED:
            case PreferenceHelper.ARE_CIRCLES_COLORED:
            case PreferenceHelper.IS_FAM_ENABLED:
                Utils.restartApp(getActivity());
                break;
        }
        return true;
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
