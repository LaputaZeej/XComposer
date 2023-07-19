package com.yunext.twins.ui.compoents.dialog

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yunext.twins.ui.compoents.China
import kotlinx.coroutines.delay

@Composable
fun CHLoadingDialog(
    text: String = "加载中",
    properties: DialogProperties = DialogProperties(
        decorFitsSystemWindows = true,
        usePlatformDefaultWidth = true,
        dismissOnBackPress = false,
        dismissOnClickOutside = true,
        securePolicy = SecureFlagPolicy.Inherit
    ),
    debug: Boolean = false,
    dimAmount: Float,
    onDismissRequest: () -> Unit,
) {
    CHDialog(properties, dimAmount = dimAmount, onDismissRequest = onDismissRequest) {
        Box(
            Modifier
                .size(DialogDefaults.DEFAULT_SIZE)
                .clip(DialogDefaults.DEFAULT_SHAPE)
                .background(China.g_zhu_lv)
                .debugShape(debug)
                .padding(16.dp)
                .debugShape(debug)

        ) {
            Column(
                Modifier
                    .wrapContentSize()
                    .align(Alignment.Center)
            ) {
                var end by remember {
                    mutableStateOf("")
                }
                LaunchedEffect(key1 = Unit) {
                    while (true) {
                        delay(500)
                        end += "."
                        if (end.length > 3) {
                            end = "."
                        }
                    }
                }

                CircularProgressIndicator(
                    color = DialogDefaults.DEFAULT_Progress_Color,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .debugShape(debug)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "$text$end",
                    fontSize = DialogDefaults.DEFAULT_FONT_Size,
                    fontWeight = FontWeight.Light,
                    color = DialogDefaults.DEFAULT_FONT_Color,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)

                        .animateContentSize(spring(Spring.DampingRatioHighBouncy))
                )
            }
        }
    }
}

@Deprecated("delete", replaceWith = ReplaceWith("TslEditor"))
@Composable
fun CHBottomSheetDialog(onDismissRequest: () -> Unit) {
    CHDialog(onDismissRequest = onDismissRequest) {
        Box(
            Modifier
                .fillMaxSize()
                .background(China.r_yan_zhi_hong)
        ) {
            Column(
                Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .background(China.g_zhu_lv)
                    .padding(16.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Text(text = "hello world")
            }
        }
    }
}


