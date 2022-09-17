package by.toggi.rxbsuir

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun RxBsuirTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val context = LocalContext.current
    val colorScheme = remember(context, darkTheme) {
        when {
            Build.VERSION.SDK_INT >= 31 -> when (darkTheme) {
                true -> dynamicDarkColorScheme(context)
                else -> dynamicLightColorScheme(context)
            }
            else -> if (darkTheme) DarkColorScheme else LightColorScheme
        }
    }
    val systemUiController = rememberSystemUiController()
    LaunchedEffect(systemUiController, darkTheme) {
        systemUiController.setSystemBarsColor(color = Color.Transparent, darkIcons = !darkTheme)
    }
    MaterialTheme(colorScheme = colorScheme, content = content)
}

private val LightColorScheme = lightColorScheme()
private val DarkColorScheme = darkColorScheme()
