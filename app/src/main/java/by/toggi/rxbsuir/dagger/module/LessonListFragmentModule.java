package by.toggi.rxbsuir.dagger.module;

import by.toggi.rxbsuir.dagger.PerFragment;
import by.toggi.rxbsuir.mvp.presenter.LessonListPresenter;
import dagger.Module;
import dagger.Provides;

@Module public class LessonListFragmentModule {

  private final LessonListPresenter.Type mType;

  public LessonListFragmentModule(LessonListPresenter.Type type) {
    mType = type;
  }

  @Provides @PerFragment LessonListPresenter.Type provideViewType() {
    return mType;
  }
}
