package by.toggi.rxbsuir.activity;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;

import com.f2prateek.rx.preferences.Preference;
import com.trello.rxlifecycle.android.RxLifecycleAndroid;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import javax.inject.Inject;
import javax.inject.Named;

import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.fragment.SettingsFragment;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SettingsActivity extends RxAppCompatActivity {

    @Inject
    @Named(PreferenceHelper.NIGHT_MODE)
    Preference<String> mNightModePreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getDelegate().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_fragment_settings, new SettingsFragment())
                .commit();

        mNightModePreference.asObservable()
                .skip(1)
                .compose(RxLifecycleAndroid.bindActivity(lifecycle()))
                .subscribe(mode -> recreate());
    }
}
