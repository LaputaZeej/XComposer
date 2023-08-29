import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable

actual fun getPlatformName(): String = "Android"

@Composable fun MainView(paddingValues: PaddingValues) = App(paddingValues)
