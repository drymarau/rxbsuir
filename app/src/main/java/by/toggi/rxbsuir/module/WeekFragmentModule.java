package by.toggi.rxbsuir.module;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;

import by.toggi.rxbsuir.Fragment;
import by.toggi.rxbsuir.adapter.LessonAdapter;
import by.toggi.rxbsuir.fragment.WeekFragment;
import dagger.Module;
import dagger.Provides;

@Module
public class WeekFragmentModule {

    private final WeekFragment mWeekFragment;

    public WeekFragmentModule(WeekFragment weekFragment) {
        mWeekFragment = weekFragment;
    }

    @Provides
    @Fragment
    LinearLayoutManager provideLinearLayoutManager() {
        return new LinearLayoutManager(mWeekFragment.getActivity());
    }

    @Provides
    @Fragment
    LessonAdapter provideScheduleAdapter() {
        return new LessonAdapter(new ArrayList<>());
    }

    @Provides
    @Fragment
    int provideWeekNumber() {
        return mWeekFragment.getWeekNumber();
    }

    @Nullable
    @Provides
    @Fragment
    String provideGroupNumber() {
        return mWeekFragment.getGroupNumber();
    }

}
