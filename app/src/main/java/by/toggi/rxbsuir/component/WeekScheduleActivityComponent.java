package by.toggi.rxbsuir.component;

import by.toggi.rxbsuir.Activity;
import by.toggi.rxbsuir.activity.WeekScheduleActivity;
import by.toggi.rxbsuir.module.WeekScheduleActivityModule;
import dagger.Component;

@Activity
@Component(dependencies = AppComponent.class, modules = {
        WeekScheduleActivityModule.class
})
public interface WeekScheduleActivityComponent {

    void inject(WeekScheduleActivity weekScheduleActivity);

}
