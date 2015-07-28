package by.toggi.rxbsuir;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import by.toggi.rxbsuir.adapter.LessonAdapter;

import static android.view.View.MeasureSpec;

public class SubheaderItemDecoration extends RecyclerView.ItemDecoration {

    private final TextView mHeaderView;
    private final int mSubheaderHeight;

    public SubheaderItemDecoration(View headerView, int subheaderHeight) {
        mHeaderView = (TextView) headerView;
        mSubheaderHeight = subheaderHeight;
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int adapterPosition = parent.getChildAdapterPosition(child);
            switch (parent.getAdapter().getItemViewType(adapterPosition)) {
                case LessonAdapter.VIEW_TYPE_LESSON_ONE_LINE_WITH_WEEKDAY:
                case LessonAdapter.VIEW_TYPE_LESSON_TWO_LINE_WITH_WEEKDAY:
                case LessonAdapter.VIEW_TYPE_LESSON_THREE_LINE_WITH_WEEKDAY:
                    LessonAdapter.ViewHolder viewHolder = (LessonAdapter.ViewHolder) parent.getChildViewHolder(child);
                    drawText(c, viewHolder.getWeekDay(), child.getTop() - mSubheaderHeight - params.topMargin);
                    break;
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int adapterPosition = parent.getChildAdapterPosition(view);
        switch (parent.getAdapter().getItemViewType(adapterPosition)) {
            case LessonAdapter.VIEW_TYPE_LESSON_ONE_LINE_WITH_WEEKDAY:
            case LessonAdapter.VIEW_TYPE_LESSON_TWO_LINE_WITH_WEEKDAY:
            case LessonAdapter.VIEW_TYPE_LESSON_THREE_LINE_WITH_WEEKDAY:
                outRect.set(0, mSubheaderHeight, 0, 0);
                break;
            default:
                super.getItemOffsets(outRect, view, parent, state);
        }
    }

    private void drawText(Canvas c, String text, float dy) {
        mHeaderView.setText(text);
        mHeaderView.measure(
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        );
        mHeaderView.layout(0, 0, mHeaderView.getMeasuredWidth(), mHeaderView.getMeasuredHeight());
        c.save();
        c.translate(0, dy);
        mHeaderView.draw(c);
        c.restore();
    }

}
