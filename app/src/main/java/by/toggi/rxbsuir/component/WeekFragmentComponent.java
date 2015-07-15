package by.toggi.rxbsuir.component;

import android.support.v7.widget.LinearLayoutManager;

import by.toggi.rxbsuir.Fragment;
import by.toggi.rxbsuir.WeekdayItemDecoration;
import by.toggi.rxbsuir.adapter.LessonAdapter;
import by.toggi.rxbsuir.fragment.WeekFragment;
import by.toggi.rxbsuir.module.WeekFragmentModule;
import dagger.Component;

@Fragment
@Component(dependencies = AppComponent.class, modules = WeekFragmentModule.class)
public interface WeekFragmentComponent {

    LinearLayoutManager linearLayoutManager();

    LessonAdapter lessonAdapter();

    WeekdayItemDecoration weekdayItemDecoration();

    void inject(WeekFragment weekFragment);

}
