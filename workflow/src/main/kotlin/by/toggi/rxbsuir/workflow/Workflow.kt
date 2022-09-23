package by.toggi.rxbsuir.workflow

import androidx.compose.runtime.Composable

public fun interface Workflow<Props, Output, Rendering> {

    @Composable
    public fun render(props: Props, onOutput: (Output) -> Unit): Rendering
}

@Composable
public fun <Rendering> Workflow<Unit, Nothing, Rendering>.render(): Rendering = render(Unit)

@Composable
public fun <Output, Rendering> Workflow<Unit, Output, Rendering>.render(onOutput: (Output) -> Unit): Rendering =
    render(Unit, onOutput)

@Composable
public fun <Props, Rendering> Workflow<Props, Nothing, Rendering>.render(props: Props): Rendering =
    render(props) { }
