package by.toggi.rxbsuir.module;

import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;

import by.toggi.rxbsuir.Activity;
import by.toggi.rxbsuir.activity.TermScheduleActivity;
import by.toggi.rxbsuir.adapter.LessonAdapter;
import dagger.Module;
import dagger.Provides;

@Module
public class TermScheduleActivityModule {

    private final TermScheduleActivity mActivity;

    public TermScheduleActivityModule(TermScheduleActivity activity) {
        mActivity = activity;
    }

    @Provides
    @Activity
    TermScheduleActivity provideActivity() {
        return mActivity;
    }

    @Provides
    @Activity
    LinearLayoutManager provideLinearLayoutManager() {
        return new LinearLayoutManager(mActivity);
    }

    @Provides
    @Activity
    LessonAdapter provideLessonAdapter() {
        return new LessonAdapter(new ArrayList<>());
    }

}
