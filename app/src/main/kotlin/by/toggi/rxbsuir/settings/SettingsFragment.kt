package by.toggi.rxbsuir.settings

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import by.toggi.rxbsuir.PreferenceHelper
import by.toggi.rxbsuir.R
import by.toggi.rxbsuir.Utils
import by.toggi.rxbsuir.dagger.PerFragment
import by.toggi.rxbsuir.util.consume
import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompatDividers
import com.takisoft.fix.support.v7.preference.TimePickerPreference
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjection
import hu.supercluster.paperwork.Paperwork
import org.threeten.bp.LocalTime
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject
import javax.inject.Named
import com.f2prateek.rx.preferences.Preference as RxPreference

class SettingsFragment : PreferenceFragmentCompatDividers() {

  private val subscriptions = CompositeSubscription()

  @Inject
  lateinit var paperwork: Paperwork
  @Inject
  lateinit var localTimePreference: RxPreference<LocalTime>
  @field:[Inject Named(PreferenceHelper.FAVORITE_SYNC_ID)]
  lateinit var favoriteSyncIdPreference: RxPreference<String>

  private lateinit var notificationTimePreference: TimePickerPreference

  override fun onAttach(context: Context?) {
    AndroidSupportInjection.inject(this)
    super.onAttach(context)
  }

  override fun onCreatePreferencesFix(savedInstanceState: Bundle?, rootKey: String?) {
    setPreferencesFromResource(R.xml.preferences, rootKey)

    findPreference("build_version").summary = paperwork["gitInfo"]
    findPreference("rate_app").setOnPreferenceClickListener {
      consume { Utils.openPlayStorePage(context) }
    }

    notificationTimePreference = findPreference("notification_time") as? TimePickerPreference
        ?: throw IllegalStateException("notification_time == null.")
    notificationTimePreference.isEnabled = favoriteSyncIdPreference.get() != null
  }

  override fun onStart() {
    super.onStart()
    subscriptions.add(localTimePreference
        .asObservable()
        .subscribe {
          notificationTimePreference.setTime(it.hour, it.minute)
          if (favoriteSyncIdPreference.get() != null) {
            Utils.setNotificationAlarm(context, it)
          }
        })
  }

  override fun onStop() {
    subscriptions.clear()
    super.onStop()
  }

  companion object {

    fun newInstance(): Fragment {
      return SettingsFragment()
    }
  }

  @dagger.Module
  interface Module {

    @PerFragment
    @ContributesAndroidInjector
    fun contribute(): SettingsFragment
  }
}
