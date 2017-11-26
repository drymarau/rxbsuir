package by.toggi.rxbsuir.util

import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction

inline fun FragmentManager.commit(actions: FragmentTransaction.() -> Unit) {
  val transaction = beginTransaction()
  transaction.actions()
  transaction.commit()
}
