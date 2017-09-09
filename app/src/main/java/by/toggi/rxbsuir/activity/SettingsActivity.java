package by.toggi.rxbsuir.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.fragment.SettingsFragment;
import com.f2prateek.rx.preferences.Preference;
import com.trello.rxlifecycle.RxLifecycle;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import javax.inject.Inject;
import javax.inject.Named;

public class SettingsActivity extends RxAppCompatActivity {

  @BindView(R.id.toolbar) Toolbar mToolbar;

  @Inject @Named(PreferenceHelper.NIGHT_MODE) Preference<String> mNightModePreference;

  @Override protected void onCreate(Bundle savedInstanceState) {
    RxBsuirApplication.getAppComponent().inject(this);
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_settings);

    ButterKnife.bind(this);
    setSupportActionBar(mToolbar);
    getDelegate().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    mToolbar.setNavigationOnClickListener(v -> onBackPressed());

    getSupportFragmentManager().beginTransaction()
        .replace(R.id.container_fragment_settings, new SettingsFragment())
        .commit();

    mNightModePreference.asObservable()
        .skip(1)
        .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
        .subscribe(mode -> recreate());
  }
}
