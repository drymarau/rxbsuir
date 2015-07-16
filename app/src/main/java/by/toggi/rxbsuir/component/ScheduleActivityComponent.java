package by.toggi.rxbsuir.component;

import by.toggi.rxbsuir.Activity;
import by.toggi.rxbsuir.activity.ScheduleActivity;
import by.toggi.rxbsuir.module.ActivityModule;
import by.toggi.rxbsuir.module.ScheduleActivityModule;
import dagger.Component;

@Activity
@Component(dependencies = AppComponent.class, modules = {
        ActivityModule.class,
        ScheduleActivityModule.class
})
public interface ScheduleActivityComponent {

    void inject(ScheduleActivity scheduleActivity);

}
