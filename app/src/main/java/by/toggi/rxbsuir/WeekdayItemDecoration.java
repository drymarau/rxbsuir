package by.toggi.rxbsuir;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class WeekdayItemDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (parent.getChildAdapterPosition(view) % 4 == 0 && parent.getChildAdapterPosition(view) != 0) {
            outRect.set(0, 50, 0, 0);
        } else {
            super.getItemOffsets(outRect, view, parent, state);
        }
    }
}
