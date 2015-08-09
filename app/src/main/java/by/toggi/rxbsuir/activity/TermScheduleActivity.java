package by.toggi.rxbsuir.activity;

import android.os.Bundle;

import by.toggi.rxbsuir.R;

public class TermScheduleActivity extends ScheduleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_term_schedule;
    }

    @Override
    protected void showToday() {

    }
}
