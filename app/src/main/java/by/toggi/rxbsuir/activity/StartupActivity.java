package by.toggi.rxbsuir.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import by.toggi.rxbsuir.RxBsuirApplication;

public class StartupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (((RxBsuirApplication) getApplication()).getAppComponent().isWeekView()) {
            startActivity(new Intent(this, WeekScheduleActivity.class));
        } else {
            startActivity(new Intent(this, TermScheduleActivity.class));
        }
        finish();
    }
}
