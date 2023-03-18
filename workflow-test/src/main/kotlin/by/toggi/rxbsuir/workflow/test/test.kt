package by.toggi.rxbsuir.workflow.test

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import app.cash.molecule.RecompositionClock
import app.cash.molecule.moleculeFlow
import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.test
import by.toggi.rxbsuir.workflow.Workflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.merge
import kotlin.test.assertIs
import kotlin.time.Duration

public suspend fun <Output, Rendering> Workflow<Unit, Output, Rendering>.test(
    timeout: Duration? = null,
    validate: suspend ReceiveTurbine<Event<Output, Rendering>>.() -> Unit
) {
    test(Unit, timeout, validate)
}

public suspend fun <Props, Output, Rendering> Workflow<Props, Output, Rendering>.test(
    props: Props,
    timeout: Duration? = null,
    validate: suspend ReceiveTurbine<Event<Output, Rendering>>.() -> Unit
) {
    test(MutableStateFlow(props), timeout, validate)
}

public suspend fun <Props, Output, Rendering> Workflow<Props, Output, Rendering>.test(
    props: StateFlow<Props>,
    timeout: Duration? = null,
    validate: suspend ReceiveTurbine<Event<Output, Rendering>>.() -> Unit
) {
    val outputFlow = MutableSharedFlow<Event.Output<Output>>(extraBufferCapacity = Int.MAX_VALUE)
    val renderingFlow = moleculeFlow(RecompositionClock.Immediate) {
        val p by props.collectAsState()
        val rendering = render(p) { output -> outputFlow.tryEmit(Event.Output(output)) }
        Event.Rendering(rendering)
    }
    merge(outputFlow, renderingFlow).test(timeout = timeout, validate = validate)
}

public suspend fun <Output, Rendering> ReceiveTurbine<Event<Output, Rendering>>.awaitOutput(): Output =
    assertIs<Event.Output<Output>>(awaitItem()).value

public suspend fun <Output, Rendering> ReceiveTurbine<Event<Output, Rendering>>.awaitRendering(): Rendering =
    assertIs<Event.Rendering<Rendering>>(awaitItem()).value
