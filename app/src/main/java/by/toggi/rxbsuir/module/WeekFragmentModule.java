package by.toggi.rxbsuir.module;

import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;

import by.toggi.rxbsuir.Fragment;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.WeekdayItemDecoration;
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
    WeekdayItemDecoration provideWeekdayItemDecoration() {
        return new WeekdayItemDecoration(mWeekFragment.getResources().getDimensionPixelSize(R.dimen.list_item_weekday_margin));
    }

    @Provides
    @Fragment
    int provideWeekNumber() {
        return mWeekFragment.getWeekNumber();
    }

}