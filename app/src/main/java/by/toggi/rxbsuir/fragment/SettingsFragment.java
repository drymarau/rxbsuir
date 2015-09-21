package by.toggi.rxbsuir.fragment;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    public void onCreate(Bundle savedInstanceState) {
        ((RxBsuirApplication) getActivity().getApplication()).getAppComponent().inject(this);

        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);

        findPreference(PreferenceHelper.IS_DARK_THEME).setOnPreferenceChangeListener(this);
        findPreference("build_version").setSummary(BuildConfig.VERSION_NAME);
        findPreference("rate_app").setOnPreferenceClickListener(this);
        findPreference(PreferenceHelper.IS_TODAY_ENABLED).setOnPreferenceChangeListener(this);
        findPreference(PreferenceHelper.ARE_CIRCLES_COLORED).setOnPreferenceChangeListener(this);
        mNotificationTimePreference = findPreference("notification_time");
        mNotificationTimePreference.setEnabled(mFavoriteSyncIdPrerefence.get() != null);
        mNotificationTimePreference.setOnPreferenceClickListener(this);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.preference_list_view, container, false);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        switch (preference.getKey()) {
            case PreferenceHelper.IS_DARK_THEME:
            case PreferenceHelper.IS_TODAY_ENABLED:
            case PreferenceHelper.ARE_CIRCLES_COLORED:
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
