import androidx.compose.ui.window.ComposeUIViewController
import com.yunext.twins.data.TextDemoPreview
import com.yunext.twins.module.repository.createRootComponent

actual fun getPlatformName(): String = "iOS"

fun MainViewController() = ComposeUIViewController { App()
//    TextDemoPreview()
}