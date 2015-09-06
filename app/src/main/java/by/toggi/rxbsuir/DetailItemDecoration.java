package by.toggi.rxbsuir;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import static by.toggi.rxbsuir.adapter.DetailItemAdapter.ViewHolder;

public class DetailItemDecoration extends RecyclerView.ItemDecoration {

    private final int mPadding;
    private final Drawable mDivider;
    private final int mMarginStart;

    public DetailItemDecoration(Context context) {
        mPadding = context.getResources().getDimensionPixelSize(R.dimen.divider_padding);
        mMarginStart = context.getResources().getDimensionPixelSize(R.dimen.keyline_second);
        final TypedArray a = context.obtainStyledAttributes(new int[]{android.R.attr.listDivider});
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        ViewHolder holder = (ViewHolder) parent.getChildViewHolder(view);
        if (holder.isLast() && holder.isFirst()) {
            outRect.set(0, mPadding, 0, mPadding + mDivider.getIntrinsicHeight());
        } else if (holder.isFirst()) {
            outRect.set(0, mPadding, 0, 0);
        } else if (holder.isLast()) {
            outRect.set(0, 0, 0, mPadding + mDivider.getIntrinsicHeight());
        } else {
            super.getItemOffsets(outRect, view, parent, state);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        final int left = parent.getPaddingLeft() + mMarginStart;
        final int right = parent.getWidth() - parent.getPaddingRight();
        final int recyclerViewTop = parent.getPaddingTop();
        final int recyclerViewBottom = parent.getHeight() - parent.getPaddingBottom();
        for (int i = 0, childCount = parent.getChildCount(); i < childCount; i++) {
            final View view = parent.getChildAt(i);
            final int adapterPosition = parent.getChildAdapterPosition(view);
            final ViewHolder holder = (ViewHolder) parent.getChildViewHolder(view);
            if (adapterPosition < parent.getAdapter().getItemCount() - 1 && holder.isLast()) {
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) view.getLayoutParams();
                final int top = Math.max(recyclerViewTop, view.getBottom() + params.bottomMargin + mPadding);
                final int bottom = Math.min(recyclerViewBottom, top + mDivider.getIntrinsicHeight());
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }
}
