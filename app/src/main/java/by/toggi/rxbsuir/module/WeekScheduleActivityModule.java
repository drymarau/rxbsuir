package by.toggi.rxbsuir.module;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import by.toggi.rxbsuir.Activity;
import by.toggi.rxbsuir.R;
import dagger.Module;
import dagger.Provides;

@Module
public class WeekScheduleActivityModule {

    @Provides
    @Activity
    String[] provideTabs(Context context) {
        return context.getResources().getStringArray(R.array.tabs);
    }

    @Provides
    @Activity
    FragmentManager provideSupportFragmentManager(AppCompatActivity activity) {
        return activity.getSupportFragmentManager();
    }

}
