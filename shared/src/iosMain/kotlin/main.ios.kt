import androidx.compose.ui.window.ComposeUIViewController
import com.yunext.twins.data.TextDemoPreview

actual fun getPlatformName(): String = "iOS"

fun MainViewController() = ComposeUIViewController { App()
//    TextDemoPreview()
}