package by.toggi.rxbsuir.dagger.component;

import by.toggi.rxbsuir.dagger.PerFragment;
import by.toggi.rxbsuir.dagger.module.LessonListFragmentModule;
import by.toggi.rxbsuir.fragment.LessonListFragment;
import dagger.Component;

@PerFragment @Component(dependencies = AppComponent.class, modules = LessonListFragmentModule.class)
public interface LessonListFragmentComponent {

  void inject(LessonListFragment lessonListFragment);
}
