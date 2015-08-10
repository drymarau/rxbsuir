package by.toggi.rxbsuir.module;

import by.toggi.rxbsuir.Activity;
import by.toggi.rxbsuir.activity.TermScheduleActivity;
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

}
