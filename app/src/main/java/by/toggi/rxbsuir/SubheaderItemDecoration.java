package by.toggi.rxbsuir;

import static android.view.View.MeasureSpec;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import by.toggi.rxbsuir.adapter.LessonAdapter;

public class SubheaderItemDecoration extends RecyclerView.ItemDecoration {

    private final TextView mHeaderView;
    private final int mSubheaderHeight;

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
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, RecyclerView parent, @NonNull RecyclerView.State state) {
        for (int i = 0, childCount = parent.getChildCount(); i < childCount; i++) {
            var child = parent.getChildAt(i);
            var params = (RecyclerView.LayoutParams) child.getLayoutParams();
            var holder = (LessonAdapter.ViewHolder) parent.getChildViewHolder(child);
            if (holder.isFirst()) {
                drawText(c, holder.getHeaderText(), child.getTop() - mSubheaderHeight - params.topMargin);
            }
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View child, RecyclerView parent, @NonNull RecyclerView.State state) {
        var holder = (LessonAdapter.ViewHolder) parent.getChildViewHolder(child);
        if (holder.isFirst()) {
            outRect.set(0, mSubheaderHeight, 0, 0);
        } else {
            super.getItemOffsets(outRect, child, parent, state);
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
