import androidx.compose.runtime.Composable
import com.yunext.twins.module.repository.createRootComponent

actual fun getPlatformName(): String = "Desktop"

@Composable fun MainView() = App()