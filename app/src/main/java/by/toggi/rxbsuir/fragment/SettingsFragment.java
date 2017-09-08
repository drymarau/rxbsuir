package by.toggi.rxbsuir.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.Preference;
import by.toggi.rxbsuir.BuildConfig;
import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.Utils;
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;
import javax.inject.Inject;
import javax.inject.Named;
import org.threeten.bp.LocalTime;
import rx.Subscription;

public class SettingsFragment extends PreferenceFragmentCompat
    implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener,
    TimePickerDialog.OnTimeSetListener {

  @Inject @Named(PreferenceHelper.IS_DARK_THEME) boolean mIsDarkTheme;
  @Inject com.f2prateek.rx.preferences.Preference<LocalTime> mLocalTimePreference;
  @Inject @Named(PreferenceHelper.FAVORITE_SYNC_ID) com.f2prateek.rx.preferences.Preference<String>
      mFavoriteSyncIdPrerefence;

  private Subscription mSubscription;
  private Preference mNotificationTimePreference;

  @Override
  public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
    ((RxBsuirApplication) getActivity().getApplication()).getAppComponent().inject(this);

    setPreferencesFromResource(R.xml.preferences, rootKey);

    findPreference(PreferenceHelper.IS_DARK_THEME).setOnPreferenceChangeListener(this);
    findPreference("build_version").setSummary(BuildConfig.VERSION_NAME);
    findPreference("rate_app").setOnPreferenceClickListener(this);
    findPreference(PreferenceHelper.IS_TODAY_ENABLED).setOnPreferenceChangeListener(this);
    findPreference(PreferenceHelper.ARE_CIRCLES_COLORED).setOnPreferenceChangeListener(this);
    findPreference(PreferenceHelper.IS_FAM_ENABLED).setOnPreferenceChangeListener(this);
    mNotificationTimePreference = findPreference("notification_time");
    mNotificationTimePreference.setEnabled(mFavoriteSyncIdPrerefence.get() != null);
    mNotificationTimePreference.setOnPreferenceClickListener(this);
  }

  @Override public void onResume() {
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

  @Override public void onPause() {
    super.onPause();
    Utils.unsubscribe(mSubscription);
  }

  @Override public boolean onPreferenceChange(Preference preference, Object newValue) {
    switch (preference.getKey()) {
      case PreferenceHelper.IS_DARK_THEME:
      case PreferenceHelper.IS_TODAY_ENABLED:
      case PreferenceHelper.ARE_CIRCLES_COLORED:
      case PreferenceHelper.IS_FAM_ENABLED:
        Utils.restartApp(getActivity());
        break;
    }
    return true;
  }

  @Override public boolean onPreferenceClick(Preference preference) {
    switch (preference.getKey()) {
      case "rate_app":
        Utils.openPlayStorePage(getActivity());
        return true;
      case "notification_time":
        LocalTime localTime = mLocalTimePreference.get();
        TimePickerDialog dialog =
            TimePickerDialog.newInstance(this, localTime.getHour(), localTime.getMinute(), true);
        dialog.setThemeDark(mIsDarkTheme);
        dialog.show(getActivity().getFragmentManager(), "time_picker");
        return true;
    }
    return false;
  }

  @Override public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
    mLocalTimePreference.set(LocalTime.of(hourOfDay, minute));
  }
}
