package by.toggi.rxbsuir.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat

fun Context.getDrawableCompat(@DrawableRes id: Int): Drawable? {
  return ContextCompat.getDrawable(this, id)
}
