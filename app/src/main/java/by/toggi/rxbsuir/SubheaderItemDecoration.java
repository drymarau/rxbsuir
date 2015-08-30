package by.toggi.rxbsuir;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import by.toggi.rxbsuir.adapter.LessonAdapter;

import static android.view.View.MeasureSpec;

public class SubheaderItemDecoration extends RecyclerView.ItemDecoration {

    private final TextView mHeaderView;
    private final int mSubheaderHeight;
    private final Drawable mShadow;

    public SubheaderItemDecoration(Context context, View headerView, int subheaderHeight) {
        mHeaderView = (TextView) headerView;
        mSubheaderHeight = subheaderHeight;
        mHeaderView.measure(
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        );
        mHeaderView.layout(0, 0, mHeaderView.getMeasuredWidth(), mHeaderView.getMeasuredHeight());
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mShadow = context.getResources().getDrawable(R.drawable.header_shadow);
        } else {
            mShadow = null;
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int adapterPosition = parent.getChildAdapterPosition(child);
            LessonAdapter.ViewHolder holder = (LessonAdapter.ViewHolder) parent.getChildViewHolder(child);
            switch (parent.getAdapter().getItemViewType(adapterPosition)) {
                case LessonAdapter.VIEW_TYPE_LESSON_ONE_LINE_WITH_WEEKDAY:
                case LessonAdapter.VIEW_TYPE_LESSON_TWO_LINE_WITH_WEEKDAY:
                case LessonAdapter.VIEW_TYPE_LESSON_THREE_LINE_WITH_WEEKDAY:
                    drawText(c, holder.getWeekDay(), child.getTop() - mSubheaderHeight - params.topMargin);

                    break;
            }
            if (holder.isLast() && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                drawShadow(c, parent, child, params);
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

    private void drawShadow(Canvas c, RecyclerView parent, View child, RecyclerView.LayoutParams params) {
        final int top = child.getBottom() + params.bottomMargin;
        final int bottom = top + mShadow.getIntrinsicHeight();
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        mShadow.setBounds(left, top, right, bottom);
        mShadow.draw(c);
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
