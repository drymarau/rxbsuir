package by.toggi.rxbsuir.component;

import android.content.Context;

import by.toggi.rxbsuir.Activity;
import by.toggi.rxbsuir.ScheduleActivity;
import by.toggi.rxbsuir.module.ActivityModule;
import dagger.Component;

@Activity
@Component(dependencies = AppComponent.class, modules = ActivityModule.class)
public interface ScheduleActivityComponent {

    Context context();

    void inject(ScheduleActivity scheduleActivity);

}
