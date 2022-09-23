package by.toggi.rxbsuir.workflow

import androidx.compose.runtime.Composable

public interface Workflow<Props, Output, Rendering> {

    @Composable
    public fun render(props: Props, onOutput: (Output) -> Unit): Rendering
}

public inline fun <Props, Output, Rendering> workflow(crossinline block: @Composable (props: Props, onOutput: (Output) -> Unit) -> Rendering): Workflow<Props, Output, Rendering> =
    object : Workflow<Props, Output, Rendering> {

        @Composable
        override fun render(props: Props, onOutput: (Output) -> Unit): Rendering =
            block(props, onOutput)
    }

@Composable
public fun <Rendering> Workflow<Unit, Nothing, Rendering>.render(): Rendering = render(Unit)

@Composable
public fun <Output, Rendering> Workflow<Unit, Output, Rendering>.render(onOutput: (Output) -> Unit): Rendering =
    render(Unit, onOutput)

@Composable
public fun <Props, Rendering> Workflow<Props, Nothing, Rendering>.render(props: Props): Rendering =
    render(props) { }
