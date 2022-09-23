package by.toggi.rxbsuir.workflow.test

public sealed interface Event<out Output, out Rendering> {

    public data class Output<T>(public val value: T) : Event<T, Nothing>
    public data class Rendering<T>(public val value: T) : Event<Nothing, T>
}
