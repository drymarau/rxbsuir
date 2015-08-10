package by.toggi.rxbsuir.component;

import by.toggi.rxbsuir.Activity;
import by.toggi.rxbsuir.activity.TermScheduleActivity;
import by.toggi.rxbsuir.module.TermScheduleActivityModule;
import dagger.Component;

@Activity
@Component(dependencies = AppComponent.class, modules = TermScheduleActivityModule.class)
public interface TermScheduleActivityComponent {

    void inject(TermScheduleActivity termScheduleActivity);

}
