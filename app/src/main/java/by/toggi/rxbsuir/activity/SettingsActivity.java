package by.toggi.rxbsuir.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import javax.inject.Inject;
import javax.inject.Named;

import butterknife.ButterKnife;
import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.fragment.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar mToolbar;

    @Inject @Named(PreferenceHelper.IS_DARK_THEME) boolean mIsDarkTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((RxBsuirApplication) getApplication()).getAppComponent().inject(this);

        setTheme(mIsDarkTheme ? R.style.AppTheme_Dark : R.style.AppTheme_Light);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ButterKnife.bind(this);
        setSupportActionBar(mToolbar);
        getDelegate().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());

        getFragmentManager().beginTransaction()
                .replace(R.id.container_fragment_settings, new SettingsFragment())
                .commit();
    }
}
