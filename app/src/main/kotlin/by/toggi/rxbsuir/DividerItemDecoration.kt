package by.toggi.rxbsuir

import android.content.Context
import android.content.res.Resources
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ItemDecoration
import android.support.v7.widget.RecyclerView.State
import android.view.View
import by.toggi.rxbsuir.util.getDrawableCompat

class DividerItemDecoration private constructor(
    context: Context,
    @DrawableRes id: Int,
    val orientation: Int
) : ItemDecoration() {

  private val Context.listDivider: Drawable
    get() {
      val attrs = obtainStyledAttributes(intArrayOf(android.R.attr.listDivider))
      val drawable = attrs.getDrawable(0)
      attrs.recycle()
      return drawable
    }

  private val bounds = Rect()
  private val divider: Drawable = when (id) {
    0 -> context.listDivider
    else -> context.getDrawableCompat(id) ?: throw IllegalArgumentException("divider == null.")
  }

  override fun onDrawOver(c: Canvas, parent: RecyclerView, state: State) {
    when (orientation) {
      LinearLayoutManager.VERTICAL -> drawVertical(c, parent)
      LinearLayoutManager.HORIZONTAL -> drawHorizontal(c, parent)
    }
  }

  private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
    val adapter = parent.adapter as? Provider ?: return
    val resources = parent.resources

    canvas.save()
    val left: Int
    val right: Int
    if (parent.clipToPadding) {
      left = parent.paddingLeft
      right = parent.width - parent.paddingRight
      canvas.clipRect(left, parent.paddingTop, right, parent.height - parent.paddingBottom)
    } else {
      left = 0
      right = parent.width
    }
    for (index in 0 until parent.childCount) {
      val child = parent.getChildAt(index)
      val position = parent.getChildAdapterPosition(child)
      if (position == RecyclerView.NO_POSITION) {
        continue
      }
      if (adapter.hasDivider(resources, position)) {
        parent.getDecoratedBoundsWithMargins(child, bounds)
        val bottom = bounds.bottom + Math.round(child.translationY)
        val top = bottom - divider.intrinsicHeight
        val translationX = Math.round(child.translationX)
        divider.setBounds(left + translationX, top, right + translationX, bottom)
        divider.alpha = Math.round(child.alpha * 255)
        divider.draw(canvas)
      }
    }
    canvas.restore()
  }

  private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
    val adapter = parent.adapter as? Provider ?: return
    val resources = parent.resources

    canvas.save()
    val top: Int
    val bottom: Int
    if (parent.clipToPadding) {
      top = parent.paddingTop
      bottom = parent.height - parent.paddingBottom
      canvas.clipRect(parent.paddingLeft, top, parent.width - parent.paddingRight, bottom)
    } else {
      top = 0
      bottom = parent.height
    }

    for (index in 0 until parent.childCount) {
      val child = parent.getChildAt(index)
      val position = parent.getChildAdapterPosition(child)
      if (position == RecyclerView.NO_POSITION) {
        continue
      }
      if (adapter.hasDivider(resources, position)) {
        parent.getDecoratedBoundsWithMargins(child, bounds)
        val right = bounds.right + Math.round(child.translationX)
        val left = right - divider.intrinsicWidth
        val translationY = Math.round(child.translationY)
        divider.setBounds(left, top + translationY, right, bottom + translationY)
        divider.alpha = Math.round(child.alpha * 255)
        divider.draw(canvas)
      }
    }
    canvas.restore()
  }

  override fun getItemOffsets(
      outRect: Rect,
      view: View,
      parent: RecyclerView,
      state: RecyclerView.State
  ) {
    super.getItemOffsets(outRect, view, parent, state)
    val adapter = parent.adapter as? Provider ?: return
    val resources = parent.resources

    val position = parent.getChildAdapterPosition(view)
    if (position == RecyclerView.NO_POSITION) {
      return
    }
    if (adapter.hasDivider(resources, position)) {
      when (orientation) {
        LinearLayoutManager.VERTICAL -> outRect.set(0, 0, 0, divider.intrinsicHeight)
        LinearLayoutManager.HORIZONTAL -> outRect.set(0, 0, divider.intrinsicWidth, 0)
      }
    }
  }

  interface Provider {

    fun hasDivider(resources: Resources, position: Int): Boolean {
      return false
    }
  }

  companion object {

    @JvmStatic
    @JvmOverloads
    fun vertical(context: Context, @DrawableRes id: Int = 0): ItemDecoration {
      return DividerItemDecoration(context, id, LinearLayoutManager.VERTICAL)
    }

    @JvmStatic
    @JvmOverloads
    fun horizontal(context: Context, @DrawableRes id: Int = 0): ItemDecoration {
      return DividerItemDecoration(context, id, LinearLayoutManager.HORIZONTAL)
    }
  }
}
