package by.toggi.rxbsuir.module;

import by.toggi.rxbsuir.Fragment;
import dagger.Module;
import dagger.Provides;

@Module
public class WeekFragmentModule {

    private final int mWeekNumber;

    public WeekFragmentModule(int weekNumber) {
        mWeekNumber = weekNumber;
    }

    @Provides
    @Fragment
    int provideWeekNumber() {
        return mWeekNumber;
    }

}
