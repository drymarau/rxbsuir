package by.toggi.rxbsuir.activity;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RemoteViews;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.Bind;
import butterknife.ButterKnife;
import by.toggi.rxbsuir.PreferenceHelper;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.RxBsuirApplication;
import by.toggi.rxbsuir.fragment.AppWidgetConfigFragment;
import by.toggi.rxbsuir.receiver.AppWidgetScheduleProvider;

public class AppWidgetConfigActivity extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar mToolbar;

    @Inject @Named(PreferenceHelper.IS_DARK_THEME) boolean mIsDarkTheme;

    private int mAppWidgetId;
    private final Intent mResultIntent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ((RxBsuirApplication) getApplication()).getAppComponent().inject(this);

        setTheme(mIsDarkTheme ? R.style.AppTheme_Dark : R.style.AppTheme_Light);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appwidget_config);

        ButterKnife.bind(this);

        setupToolbar();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        mResultIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_CANCELED, mResultIntent);

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }

        getFragmentManager().beginTransaction()
                .replace(R.id.container_fragment_settings, AppWidgetConfigFragment.newInstance(mAppWidgetId))
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_appwidget_config_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_done:
                RemoteViews remoteViews = AppWidgetScheduleProvider.getRemoteViews(this, mAppWidgetId);
                if (remoteViews != null) {
                    AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
                    appWidgetManager.updateAppWidget(mAppWidgetId, remoteViews);
                }

                setResult(RESULT_OK, mResultIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar() {
        getDelegate().setSupportActionBar(mToolbar);
        getDelegate().getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationIcon(R.drawable.ic_action_discard);
        mToolbar.setNavigationOnClickListener(view -> finish());
    }
}
