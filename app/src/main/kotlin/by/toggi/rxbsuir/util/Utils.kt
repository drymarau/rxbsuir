package by.toggi.rxbsuir.util

inline fun consume(actions: () -> Unit): Boolean {
  actions()
  return true
}
