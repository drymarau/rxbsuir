package by.toggi.rxbsuir.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.Preference;

import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;
import com.takisoft.fix.support.v7.preference.TimePickerPreference;

import java.time.LocalTime;

import javax.inject.Inject;
import javax.inject.Named;

import by.toggi.rxbsuir.BuildConfig;
import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.Utils;
import by.toggi.rxbsuir.dagger.PerFragment;
import dagger.android.ContributesAndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import rx.Subscription;
import timber.log.Timber;

public class SettingsFragment extends PreferenceFragmentCompat
    implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

  @Inject com.f2prateek.rx.preferences.Preference<LocalTime> mLocalTimePreference;
  @Inject @Named(PreferenceHelper.FAVORITE_SYNC_ID) com.f2prateek.rx.preferences.Preference<String>
      mFavoriteSyncIdPrerefence;

  private Subscription mSubscription;
  private TimePickerPreference mNotificationTimePreference;

  @Override public void onAttach(Context context) {
    AndroidSupportInjection.inject(this);
    super.onAttach(context);
  }

  @Override public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey) {
    setPreferencesFromResource(R.xml.preferences, rootKey);

    findPreference("build_version").setSummary(BuildConfig.VERSION_NAME);
    findPreference("rate_app").setOnPreferenceClickListener(this);
    findPreference("privacy_policy").setOnPreferenceClickListener(this);
    findPreference(PreferenceHelper.IS_TODAY_ENABLED).setOnPreferenceChangeListener(this);
    findPreference(PreferenceHelper.ARE_CIRCLES_COLORED).setOnPreferenceChangeListener(this);
    findPreference(PreferenceHelper.IS_FAM_ENABLED).setOnPreferenceChangeListener(this);
    mNotificationTimePreference = (TimePickerPreference) findPreference("notification_time");
    mNotificationTimePreference.setEnabled(mFavoriteSyncIdPrerefence.get() != null);
  }

  @Override public void onResume() {
    super.onResume();
    mSubscription = mLocalTimePreference.asObservable().subscribe(localTime -> {
      Timber.w(localTime.toString());
      if (mNotificationTimePreference != null) {
        mNotificationTimePreference.setTime(localTime.getHour(), localTime.getMinute());
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
      case "privacy_policy":
        Utils.openPrivacyPolicyPage(getActivity());
        return true;
    }
    return false;
  }

  @dagger.Module public interface Module {

    @PerFragment @ContributesAndroidInjector SettingsFragment contribute();
  }
}
