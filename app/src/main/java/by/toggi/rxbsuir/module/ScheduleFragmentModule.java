package by.toggi.rxbsuir.module;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;

import by.toggi.rxbsuir.Fragment;
import by.toggi.rxbsuir.R;
import by.toggi.rxbsuir.WeekdayItemDecoration;
import by.toggi.rxbsuir.adapter.LessonAdapter;
import dagger.Module;
import dagger.Provides;

@Module
public class ScheduleFragmentModule {

    private final Context mContext;

    public ScheduleFragmentModule(Context context) {
        mContext = context;
    }

    @Provides
    @Fragment
    LinearLayoutManager provideLinearLayoutManager() {
        return new LinearLayoutManager(mContext);
    }

    @Provides
    @Fragment
    LessonAdapter provideScheduleAdapter() {
        return new LessonAdapter(new ArrayList<>());
    }

    @Provides
    @Fragment
    WeekdayItemDecoration provideWeekdayItemDecoration() {
        return new WeekdayItemDecoration(mContext.getResources().getDimensionPixelSize(R.dimen.list_item_weekday_margin));
    }

}
