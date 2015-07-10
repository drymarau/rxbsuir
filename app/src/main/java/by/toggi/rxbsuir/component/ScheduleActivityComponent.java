package by.toggi.rxbsuir.component;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import java.util.List;

import by.toggi.rxbsuir.Activity;
import by.toggi.rxbsuir.activity.ScheduleActivity;
import by.toggi.rxbsuir.adapter.ScheduleAdapter;
import by.toggi.rxbsuir.model.Schedule;
import by.toggi.rxbsuir.module.ActivityModule;
import by.toggi.rxbsuir.module.ScheduleActivityModule;
import dagger.Component;

@Activity
@Component(dependencies = AppComponent.class, modules = {
        ActivityModule.class,
        ScheduleActivityModule.class
})
public interface ScheduleActivityComponent {

    Context context();
    LinearLayoutManager manager();
    ScheduleAdapter adapter();
    List<Schedule> scheduleList();

    void inject(ScheduleActivity scheduleActivity);

}
