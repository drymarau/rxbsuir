package by.toggi.rxbsuir;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import by.toggi.rxbsuir.adapter.LessonAdapter;

public class WeekdayItemDecoration extends RecyclerView.ItemDecoration {

    private final int mWeekdayMargin;

    public WeekdayItemDecoration(int weekdayMargin) {
        mWeekdayMargin = weekdayMargin;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int adapterPosition = parent.getChildAdapterPosition(view);
        if (adapterPosition != 0) {
            int viewType = parent.getAdapter().getItemViewType(adapterPosition);
            if (viewType == LessonAdapter.VIEW_TYPE_LESSON_WITH_WEEKDAY) {
                outRect.set(0, mWeekdayMargin, 0, 0);
                return;
            }
        }
        outRect.set(0, 0, 0, 0);
    }


}
