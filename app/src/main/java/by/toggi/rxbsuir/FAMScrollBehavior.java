package by.toggi.rxbsuir;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

@SuppressWarnings("unused")
public class FAMScrollBehavior extends CoordinatorLayout.Behavior<ViewGroup> {

    private static final Interpolator FAST_OUT_SLOW_IN_INTERPOLATOR = new FastOutSlowInInterpolator();
    private final String mTag;
    private float mTranslationY;
    private FloatingActionButton mButton;

    public FAMScrollBehavior(String tag) {
        mTag = tag;
    }

    public FAMScrollBehavior(Context context, AttributeSet attrs) {
        mTag = context.getString(R.string.tag_fab);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, ViewGroup child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, ViewGroup child, View dependency) {
        if (dependency instanceof Snackbar.SnackbarLayout) {
            this.updateFabTranslationForSnackbar(parent, child);
        }
        return false;
    }

    @Override
    public void onDependentViewRemoved(CoordinatorLayout parent, ViewGroup child, View dependency) {
        if (dependency instanceof Snackbar.SnackbarLayout) {
            ViewCompat.animate(child).translationY(0.0F).setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR).setListener(null);
        }
    }

    private void updateFabTranslationForSnackbar(CoordinatorLayout parent, ViewGroup child) {
        if (child.getVisibility() == View.VISIBLE) {
            var translationY = this.getFabTranslationYForSnackbar(parent, child);
            if (translationY != this.mTranslationY) {
                ViewCompat.animate(child).cancel();
                ViewCompat.setTranslationY(child, translationY);
                this.mTranslationY = translationY;
            }

        }
    }

    private float getFabTranslationYForSnackbar(CoordinatorLayout parent, ViewGroup child) {
        var minOffset = 0.0F;
        var dependencies = parent.getDependencies(child);
        var i = 0;

        for (int z = dependencies.size(); i < z; ++i) {
            var view = (View) dependencies.get(i);
            if (view instanceof Snackbar.SnackbarLayout && parent.doViewsOverlap(child, view)) {
                minOffset = Math.min(minOffset, ViewCompat.getTranslationY(view) - (float) view.getHeight());
            }
        }

        return minOffset;
    }

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, ViewGroup child, int layoutDirection) {
        parent.onLayoutChild(child, layoutDirection);
        return true;
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, ViewGroup child, View directTargetChild, View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target,
                        nestedScrollAxes);
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout, ViewGroup child, View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed);

        if (mButton == null) {
            mButton = child.findViewWithTag(mTag);
        }

        if (dyConsumed > 0 && mButton.getVisibility() == View.VISIBLE) {
            mButton.hide();
            mButton.setClickable(false);
        } else if (dyConsumed < 0 && mButton.getVisibility() != View.VISIBLE) {
            mButton.show();
            mButton.setClickable(true);
        }
    }
}
