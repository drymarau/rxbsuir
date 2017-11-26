package by.toggi.rxbsuir.settings

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import by.toggi.rxbsuir.PreferenceHelper
import by.toggi.rxbsuir.R
import by.toggi.rxbsuir.dagger.PerActivity
import by.toggi.rxbsuir.fragment.SettingsFragment
import by.toggi.rxbsuir.util.commit
import com.f2prateek.rx.preferences.Preference
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import rx.subscriptions.CompositeSubscription
import javax.inject.Inject
import javax.inject.Named

class SettingsActivity : AppCompatActivity(), HasSupportFragmentInjector {

  private val subscriptions = CompositeSubscription()

  @Inject
  lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>
  @field:[Inject Named(PreferenceHelper.NIGHT_MODE)]
  lateinit var nightModePreference: Preference<String>

  override fun onCreate(savedInstanceState: Bundle?) {
    AndroidInjection.inject(this)
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_settings)
    val toolbar = findViewById<Toolbar>(R.id.toolbar)
        ?: throw IllegalStateException("toolbar == null.")
    toolbar.setNavigationOnClickListener { onBackPressed() }

    val fm = supportFragmentManager
    fm.commit {
      if (fm.findFragmentById(R.id.container) == null) {
        replace(R.id.container, SettingsFragment.newInstance())
      }
    }

    subscriptions.add(nightModePreference
        .asObservable()
        .skip(1)
        .subscribe { recreate() })
  }

  override fun onDestroy() {
    super.onDestroy()
    subscriptions.clear()
  }

  override fun supportFragmentInjector(): AndroidInjector<Fragment> {
    return fragmentInjector
  }

  @dagger.Module(includes = arrayOf(SettingsFragment.Module::class))
  interface Module {

    @PerActivity
    @ContributesAndroidInjector
    fun contribute(): SettingsActivity
  }
}
