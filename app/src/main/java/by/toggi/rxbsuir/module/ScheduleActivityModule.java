package by.toggi.rxbsuir.module;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

import by.toggi.rxbsuir.Activity;
import by.toggi.rxbsuir.adapter.ScheduleAdapter;
import by.toggi.rxbsuir.model.Schedule;
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
    ScheduleAdapter provideScheduleAdapter(List<Schedule> scheduleList) {
        return new ScheduleAdapter(scheduleList);
    }

    @Provides
    @Activity
    List<Schedule> provideScheduleList() {
        return new ArrayList<>();
    }

}
