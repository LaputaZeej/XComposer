package com.yunext.twins.ui.compoents

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.FixedScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yunext.twins.ui.theme.x98f
import com.yunext.twins.ui.theme.x9f8
import com.yunext.twins.ui.theme.xf98
import kotlin.random.Random

val xplBrush = Brush.linearGradient(listOf(x9f8, x98f, xf98))

@Composable
fun XplSpacer(hor: Boolean = true) {
    Spacer(modifier = Modifier
        .background(xplBrush)
        .let {
            if (hor) {
                it
                    .fillMaxWidth()
                    .height(16.dp)
            } else {
                it
                    .fillMaxHeight()
                    .width(16.dp)
            }
        }
        .padding(top = 8.dp, bottom = 8.dp)

    )
}

fun xplRandomColor() = Color(
    Random.nextFloat(),
    Random.nextFloat(),
    Random.nextFloat(),
)

fun Modifier.xplBorder(width: Dp = 4.dp) = this.border(width, xplRandomColor())


private val contentScales =
    arrayOf(
        ContentScale.Fit,
        ContentScale.Crop,
        ContentScale.FillBounds,
        ContentScale.FillWidth,
        ContentScale.Inside,
        ContentScale.None,
    )

fun randomContentScale(): ContentScale =
    (contentScales + FixedScale(Random.nextFloat())).random()


val ContentScale.desc: String
    get() {
        return when (this) {
            ContentScale.Fit -> "Fit"
            ContentScale.Crop -> "Crop"
            ContentScale.FillBounds -> "FillBounds"
            ContentScale.FillWidth -> "FillWidth"
            ContentScale.Inside -> "Inside"
            ContentScale.None -> "None"
            is FixedScale -> "FixedScale(${this.value})"
            else -> {
                this.toString()
            }
        }
    }



@Composable
fun XTitle(title: String, desc: String="") {
    Column(Modifier.padding(top = 16.dp, bottom = 16.dp)) {
        Text(text = title, fontWeight = FontWeight.Bold, fontSize = 28.sp, color = China.z_wu_mei_zi)
        Text(
            text = if (desc.isEmpty()) "" else "「$desc」",
            fontWeight = FontWeight.Light,
            fontSize = 12.sp,
            color = China.g_lan_lv
        )
    }
}


@Composable
fun XTips(tips: String) {
    Surface(
        modifier = Modifier.padding(8.dp),
        shadowElevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
        color = China.g_zhu_lv
    ) {


        Row(
            modifier = Modifier
//            .clip(RoundedCornerShape(16.dp))
//            .background(China.zhu_lv)
                .padding(16.dp)


        ) {
            Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = China.h_gu_huang)
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "注意：$tips",
                color = China.w_qian_shi_bai,
            )
        }
    }
}


@Composable
fun XHuaLiDeFenGeXian(msg:String) {
    Row(Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 12.dp)) {
        Spacer(modifier = Modifier.height(0.5.dp).fillMaxWidth().weight(1f).background(China.hui_xiao_hui).align(Alignment.CenterVertically))
        Text(text = msg, color = China.hui_xiao_hui, fontWeight = FontWeight.Light)
        Spacer(modifier = Modifier.height(0.5.dp).fillMaxWidth().weight(1f).background(China.hui_xiao_hui).align(Alignment.CenterVertically))
    }
}