package by.toggi.rxbsuir.module;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;

import by.toggi.rxbsuir.Activity;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.adapter.ScheduleAdapter;
import dagger.Module;
import dagger.Provides;

@Module
public class ScheduleActivityModule {

    @Provides
    @Activity
    LinearLayoutManager provideLinearLayoutManager(Context context) {
        return new LinearLayoutManager(context);
    }

    @Provides
    @Activity
    ScheduleAdapter provideScheduleAdapter() {
        return new ScheduleAdapter(new ArrayList<>());
    }

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
