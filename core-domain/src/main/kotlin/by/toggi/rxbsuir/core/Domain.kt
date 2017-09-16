package by.toggi.rxbsuir.core

import io.reactivex.ObservableTransformer

typealias UseCase<T, R> = ObservableTransformer<T, R>
