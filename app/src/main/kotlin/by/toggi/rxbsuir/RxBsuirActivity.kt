package by.toggi.rxbsuir

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.squareup.workflow1.ui.ViewEnvironment
import com.squareup.workflow1.ui.WorkflowUiExperimentalApi
import com.squareup.workflow1.ui.compose.WorkflowRendering
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@OptIn(WorkflowUiExperimentalApi::class)
@AndroidEntryPoint
class RxBsuirActivity : ComponentActivity() {

    @Inject
    lateinit var viewEnvironment: ViewEnvironment

    private val viewModel by viewModels<RxBsuirViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        viewModel.output
            .onEach(::handle)
            .launchIn(lifecycleScope)
        setContent {
            val rendering by viewModel.rendering.collectAsState()
            RxBsuirTheme {
                WorkflowRendering(
                    rendering = rendering,
                    viewEnvironment = viewEnvironment
                )
            }
        }
    }

    private fun handle(output: RxBsuirOutput) = when (output) {
        is RxBsuirOutput.OnBack -> finish()
    }
}
