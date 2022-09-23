package by.toggi.rxbsuir.workflow.test

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import by.toggi.rxbsuir.workflow.Workflow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
internal class WorkflowTest {

    private lateinit var workflow: TestWorkflow

    @BeforeTest
    fun setUp() {
        workflow = TestWorkflow()
    }

    @Test
    fun `correctly runs test on the workflow`() = runTest {
        workflow.test(Props()) {
            var rendering = awaitRendering()
            assertEquals(0, rendering.accumulator)
            rendering.onIncrement()
            rendering = awaitRendering()
            assertEquals(1, rendering.accumulator)
            val output = awaitOutput()
            assertEquals("Odd!", output)
            rendering.onDecrement()
            rendering = awaitRendering()
            assertEquals(0, rendering.accumulator)
        }
    }

    private class TestWorkflow : Workflow<Props, String, Rendering> {

        @Composable
        override fun render(props: Props, onOutput: (String) -> Unit): Rendering {
            var accumulator by remember(props) { mutableStateOf(props.initialAccumulator) }
            val currentOnOutput by rememberUpdatedState(onOutput)
            LaunchedEffect(accumulator) {
                if (accumulator % 2 != 0) {
                    currentOnOutput("Odd!")
                }
            }
            return Rendering(
                accumulator = accumulator,
                onIncrement = { accumulator += 1 },
                onDecrement = { accumulator -= 1 }
            )
        }
    }

    @JvmInline
    private value class Props(val initialAccumulator: Int = 0)

    private data class Rendering(
        val accumulator: Int,
        val onIncrement: () -> Unit,
        val onDecrement: () -> Unit
    )
}
