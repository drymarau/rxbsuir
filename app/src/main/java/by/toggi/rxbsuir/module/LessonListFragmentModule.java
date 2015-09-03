package by.toggi.rxbsuir.module;

import by.toggi.rxbsuir.Fragment;
import by.toggi.rxbsuir.mvp.presenter.LessonListPresenter;
import dagger.Module;
import dagger.Provides;

@Module
public class LessonListFragmentModule {

    private final LessonListPresenter.Type mType;

    public LessonListFragmentModule(LessonListPresenter.Type type) {
        mType = type;
    }

    @Provides
    @Fragment
    LessonListPresenter.Type provideViewType() {
        return mType;
    }

}