import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Preview()
@Composable
fun DemoPreview() {
    Demo()
}

@Composable
private fun Demo() {
    Text("从入门到放弃", modifier = Modifier.size(200.dp).background(Color.LightGray))
}