package by.toggi.rxbsuir.core

sealed class Result<out V : Any, out E : Exception> {

  data class Success<out V : Any, out E : Exception>(val value: V) : Result<V, E>()
  data class Failure<out V : Any, out E : Exception>(val error: E) : Result<V, E>()

  inline fun <R> fold(success: (V) -> R, failure: (E) -> R): R {
    return when (this) {
      is Result.Success -> success(value)
      is Result.Failure -> failure(error)
    }
  }

  companion object {

    inline fun <reified V : Any, reified E : Exception> of(f: () -> V): Result<V, E> {
      return try {
        Result.Success(f())
      } catch (e: Exception) {
        if (e is E) {
          Result.Failure(e)
        } else {
          throw e
        }
      }
    }
  }
}

inline fun <V : Any, E : Exception> Result<V, E>.success(success: (V) -> Unit) {
  fold(success, {})
}

inline fun <V : Any, E : Exception> Result<V, E>.failure(failure: (E) -> Unit) {
  fold({}, failure)
}

inline fun <V : Any, U : Any, E : Exception> Result<V, E>.map(transform: (V) -> U): Result<U, E> {
  return when (this) {
    is Result.Success -> Result.Success(transform(value))
    is Result.Failure -> Result.Failure(error)
  }
}

inline fun <V : Any, U : Any, E : Exception> Result<V, E>.flatMap(
    transform: (V) -> Result<U, E>
): Result<U, E> {
  return when (this) {
    is Result.Success -> transform(value)
    is Result.Failure -> Result.Failure(error)
  }
}
