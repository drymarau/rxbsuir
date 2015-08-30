package by.toggi.rxbsuir;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import by.toggi.rxbsuir.adapter.LessonAdapter;

import static android.view.View.MeasureSpec;

public class SubheaderItemDecoration extends RecyclerView.ItemDecoration {

    private final TextView mHeaderView;
    private final int mSubheaderHeight;
    private Drawable mBottomShadow;
    private Drawable mTopShadow;

    public SubheaderItemDecoration(Context context, RecyclerView recyclerView) {
        mHeaderView = (TextView) LayoutInflater.from(context).inflate(
                R.layout.list_item_subheader,
                recyclerView,
                false
        );
        mSubheaderHeight = context.getResources().getDimensionPixelSize(R.dimen.list_subheader_height);
        mHeaderView.measure(
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        );
        mHeaderView.layout(0, 0, mHeaderView.getMeasuredWidth(), mHeaderView.getMeasuredHeight());
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mBottomShadow = ContextCompat.getDrawable(context, R.drawable.header_shadow);
            mTopShadow = ContextCompat.getDrawable(context, R.drawable.top_shadow);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        final int left = parent.getPaddingLeft();
        final int right = parent.getWidth() - parent.getPaddingRight();
        for (int i = 0, childCount = parent.getChildCount(); i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            LessonAdapter.ViewHolder holder = (LessonAdapter.ViewHolder) parent.getChildViewHolder(child);
            if (holder.isFirst()) {
                drawText(c, holder.getWeekDay(), child.getTop() - mSubheaderHeight - params.topMargin);
            }
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                if (holder.isLast()) {
                    drawBottomShadow(c, child, left, right, params);
                }
                if (holder.isFirst()) {
                    drawTopShadow(c, child, left, right);
                }
            }
        }
    }

    @Override
    public void getItemOffsets(Rect outRect, View child, RecyclerView parent, RecyclerView.State state) {
        LessonAdapter.ViewHolder holder = (LessonAdapter.ViewHolder) parent.getChildViewHolder(child);
        if (holder.isFirst()) {
            outRect.set(0, mSubheaderHeight, 0, 0);
        } else {
            super.getItemOffsets(outRect, child, parent, state);
        }
    }

    private void drawBottomShadow(Canvas c, View child, int left, int right, RecyclerView.LayoutParams params) {
        final int top = child.getBottom() + params.bottomMargin;
        final int bottom = top + mBottomShadow.getIntrinsicHeight();
        mBottomShadow.setBounds(left, top, right, bottom);
        mBottomShadow.draw(c);
    }

    private void drawTopShadow(Canvas c, View child, int left, int right) {
        final int top = child.getTop() - mTopShadow.getIntrinsicHeight();
        final int bottom = child.getTop();
        mTopShadow.setBounds(left, top, right, bottom);
        mTopShadow.draw(c);
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
