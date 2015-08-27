package by.toggi.rxbsuir.fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.threeten.bp.LocalTime;

import javax.inject.Inject;
import javax.inject.Named;

import by.toggi.rxbsuir.BuildConfig;
import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.Utils;
import rx.Subscription;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener, TimePickerDialog.OnTimeSetListener {

    @Inject @Named(PreferenceHelper.IS_DARK_THEME) boolean mIsDarkTheme;
    @Inject com.f2prateek.rx.preferences.Preference<LocalTime> mLocalTimePreference;
    @Inject @Named(PreferenceHelper.FAVORITE_SYNC_ID) com.f2prateek.rx.preferences.Preference<String> mFavoriteSyncIdPrerefence;

    private Subscription mSubscription;
    private Preference mNotificationTimePreference;

    @Override
    public void onResume() {
        super.onResume();
        mSubscription = mLocalTimePreference.asObservable()
                .subscribe(localTime -> {
                    if (mNotificationTimePreference != null) {
                        mNotificationTimePreference.setSummary(localTime.toString());
                    }
                    if (mFavoriteSyncIdPrerefence.get() != null) {
                        Utils.setAlarm(getActivity(), localTime);
                    }
                });
    }

    @Override
    public void onPause() {
        super.onPause();
        Utils.unsubscribe(mSubscription);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        ((RxBsuirApplication) getActivity().getApplication()).getAppComponent().inject(this);

        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        findPreference(PreferenceHelper.IS_DARK_THEME).setOnPreferenceChangeListener(this);
        findPreference("build_version").setSummary(BuildConfig.VERSION_NAME);
        findPreference("rate_app").setOnPreferenceClickListener(this);
        mNotificationTimePreference = findPreference("notification_time");
        mNotificationTimePreference.setEnabled(mFavoriteSyncIdPrerefence.get() != null);
        mNotificationTimePreference.setOnPreferenceClickListener(this);
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
            case "notification_time":
                LocalTime localTime = mLocalTimePreference.get();
                TimePickerDialog dialog = TimePickerDialog.newInstance(this, localTime.getHour(), localTime.getMinute(), true);
                dialog.setThemeDark(mIsDarkTheme);
                dialog.show(getFragmentManager(), "time_picker");
                return true;
        }
        return false;
    }

    @Override
    public void onTimeSet(RadialPickerLayout radialPickerLayout, int hour, int minute) {
        mLocalTimePreference.set(LocalTime.of(hour, minute));
    }
}
