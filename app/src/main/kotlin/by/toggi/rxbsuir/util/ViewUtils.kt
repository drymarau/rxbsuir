package by.toggi.rxbsuir.util

import android.view.LayoutInflater
import android.view.View

inline var View.isVisible: Boolean
  get() = visibility == View.VISIBLE
  set(value) {
    visibility = if (value) View.VISIBLE else View.GONE
  }

inline val View.inflater: LayoutInflater
  get() = LayoutInflater.from(context)
