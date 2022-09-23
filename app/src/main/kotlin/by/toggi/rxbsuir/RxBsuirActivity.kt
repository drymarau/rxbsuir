package by.toggi.rxbsuir

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import by.toggi.rxbsuir.screen.home.HomeScreen
import by.toggi.rxbsuir.screen.studentgroups.StudentGroupsScreen
import by.toggi.rxbsuir.workflow.render
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RxBsuirActivity : ComponentActivity() {

    @Inject
    lateinit var workflow: RxBsuirWorkflow

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val rendering = workflow.render(::handle)
            RxBsuirTheme {
                render(rendering)
            }
        }
    }

    @Composable
    private fun render(screen: Any, modifier: Modifier = Modifier) {
        when (screen) {
            is HomeScreen -> HomeScreen(screen, modifier)
            is StudentGroupsScreen -> StudentGroupsScreen(screen, modifier)
        }
    }

    private fun handle(output: RxBsuirOutput) = when (output) {
        is RxBsuirOutput.OnBack -> finish()
    }
}
