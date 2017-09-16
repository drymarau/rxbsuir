package by.toggi.rxbsuir.dagger.component;

import by.toggi.rxbsuir.dagger.PerFragment;
import by.toggi.rxbsuir.fragment.LessonListFragment;
import by.toggi.rxbsuir.mvp.presenter.LessonListPresenter;
import dagger.BindsInstance;
import dagger.Subcomponent;

@PerFragment @Subcomponent public interface LessonListFragmentComponent {

  void inject(LessonListFragment lessonListFragment);

  @Subcomponent.Builder interface Builder {

    @BindsInstance Builder type(LessonListPresenter.Type type);

    LessonListFragmentComponent build();
  }
}
