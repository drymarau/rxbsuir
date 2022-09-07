package by.toggi.rxbsuir;

import static by.toggi.rxbsuir.adapter.DetailItemAdapter.ViewHolder;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DetailItemDecoration extends RecyclerView.ItemDecoration {

    private final int mPadding;
    private final Drawable mDivider;
    private final int mMarginStart;

    public DetailItemDecoration(Context context) {
        mPadding = context.getResources().getDimensionPixelSize(R.dimen.divider_padding);
        mMarginStart = context.getResources().getDimensionPixelSize(R.dimen.keyline_second);
        final var a = context.obtainStyledAttributes(new int[]{android.R.attr.listDivider});
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, RecyclerView parent, @NonNull RecyclerView.State state) {
        var holder = (ViewHolder) parent.getChildViewHolder(view);
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
    public void onDrawOver(@NonNull Canvas c, RecyclerView parent, @NonNull RecyclerView.State state) {
        final var left = parent.getPaddingLeft() + mMarginStart;
        final var right = parent.getWidth() - parent.getPaddingRight();
        final var recyclerViewTop = parent.getPaddingTop();
        final var recyclerViewBottom = parent.getHeight() - parent.getPaddingBottom();
        for (int i = 0, childCount = parent.getChildCount(); i < childCount; i++) {
            final var view = parent.getChildAt(i);
            final var adapterPosition = parent.getChildAdapterPosition(view);
            final var holder = (ViewHolder) parent.getChildViewHolder(view);
            if (adapterPosition < parent.getAdapter().getItemCount() - 1 && holder.isLast()) {
                final var params = (RecyclerView.LayoutParams) view.getLayoutParams();
                final var top = Math.max(recyclerViewTop, view.getBottom() + params.bottomMargin + mPadding);
                final var bottom = Math.min(recyclerViewBottom, top + mDivider.getIntrinsicHeight());
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }
    }
}
