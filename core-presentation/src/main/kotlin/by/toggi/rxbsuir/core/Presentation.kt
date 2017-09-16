package by.toggi.rxbsuir.core

import io.reactivex.disposables.Disposable

interface View<in S> {

  fun render(state: S)

  fun report(error: Throwable)
}

interface Presenter<in S, in V : View<S>> {

  fun attach(view: V): Disposable
}

typealias Reducer<S, P> = (S, P) -> S
