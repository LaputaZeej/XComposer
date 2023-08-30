import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import com.yunext.twins.ui.AppDestination
import com.yunext.twins.ui.HomeDestination

actual fun getPlatformName(): String = "Android"

@Composable fun MainView(paddingValues: PaddingValues) = App(paddingValues)
