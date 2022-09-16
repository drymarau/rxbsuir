package by.toggi.rxbsuir.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.f2prateek.rx.preferences.Preference;

import javax.inject.Inject;
import javax.inject.Named;

import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.Utils;
import by.toggi.rxbsuir.fragment.SettingsFragment;
import dagger.hilt.android.AndroidEntryPoint;
import rx.Subscription;

@AndroidEntryPoint
public class SettingsActivity extends AppCompatActivity {

    @Inject
    @Named(PreferenceHelper.NIGHT_MODE)
    Preference<String> mNightModePreference;

    private Subscription mSubscription;

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

        mSubscription = mNightModePreference.asObservable()
                .skip(1)
                .subscribe(mode -> recreate());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Utils.unsubscribe(mSubscription);
    }
}
