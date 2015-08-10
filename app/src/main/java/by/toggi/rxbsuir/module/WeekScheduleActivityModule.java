package by.toggi.rxbsuir.module;

import android.support.v4.app.FragmentManager;

import by.toggi.rxbsuir.Activity;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.activity.WeekScheduleActivity;
import dagger.Module;
import dagger.Provides;

@Module
public class WeekScheduleActivityModule {

    private final WeekScheduleActivity mActivity;

    public WeekScheduleActivityModule(WeekScheduleActivity activity) {
        mActivity = activity;
    }

    @Provides
    @Activity
    WeekScheduleActivity provideActivity() {
        return mActivity;
    }

    @Provides
    @Activity
    String[] provideTabs() {
        return mActivity.getResources().getStringArray(R.array.tabs);
    }

    @Provides
    @Activity
    FragmentManager provideSupportFragmentManager() {
        return mActivity.getSupportFragmentManager();
    }

}
