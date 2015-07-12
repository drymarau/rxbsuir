package by.toggi.rxbsuir.component;

import android.support.v7.widget.LinearLayoutManager;

import by.toggi.rxbsuir.Fragment;
import by.toggi.rxbsuir.WeekdayItemDecoration;
import by.toggi.rxbsuir.adapter.ScheduleAdapter;
import by.toggi.rxbsuir.fragment.ScheduleFragment;
import by.toggi.rxbsuir.module.ScheduleFragmentModule;
import dagger.Component;

@Fragment
@Component(dependencies = AppComponent.class, modules = ScheduleFragmentModule.class)
public interface ScheduleFragmentComponent {

    LinearLayoutManager linearLayoutManager();

    ScheduleAdapter scheduleAdapter();

    WeekdayItemDecoration weekdayItemDecoration();

    void inject(ScheduleFragment scheduleFragment);

}
