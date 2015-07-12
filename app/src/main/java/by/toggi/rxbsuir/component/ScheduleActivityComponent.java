package by.toggi.rxbsuir.component;

import android.content.Context;
import android.support.v4.app.FragmentManager;

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

    Context context();

    String[] tabs();

    FragmentManager supportFragmentManager();

    void inject(ScheduleActivity scheduleActivity);

}
