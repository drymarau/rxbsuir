package by.toggi.rxbsuir.component;

import by.toggi.rxbsuir.Fragment;
import by.toggi.rxbsuir.fragment.LessonListFragment;
import by.toggi.rxbsuir.module.LessonListFragmentModule;
import dagger.Component;

@Fragment
@Component(dependencies = AppComponent.class, modules = LessonListFragmentModule.class)
public interface LessonListFragmentComponent {

    void inject(LessonListFragment lessonListFragment);

}
