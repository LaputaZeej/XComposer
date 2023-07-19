package com.yunext.twins.ui.compoents.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yunext.twins.ui.compoents.China
import com.yunext.twins.ui.compoents.DividerBlock
import com.yunext.twins.ui.compoents.clickableX
import com.yunext.twins.ui.theme.app_textColor_333333


@Composable
fun CHTipsDialog(
    text: String = "加载中",
    properties: DialogProperties = DialogProperties(
        decorFitsSystemWindows = true,
        usePlatformDefaultWidth = true,
        dismissOnBackPress = false,
        dismissOnClickOutside = true,
        securePolicy = SecureFlagPolicy.Inherit
    ),
    debug: Boolean = false,
    dimAmount: Float = .5f,
    onDismissRequest: () -> Unit,
) {
    CHDialog(properties, dimAmount = dimAmount, onDismissRequest = onDismissRequest) {
        Box(
            Modifier
                .fillMaxWidth()
                .padding(DialogDefaults.DEFAULT_Padding_hor)
                .aspectRatio(16 / 9f)
                .clip(DialogDefaults.DEFAULT_SHAPE)
                .background(China.w_xue_bai)
                .debugShape(debug)
//                .padding(16.dp)
                .debugShape(debug), contentAlignment = Alignment.Center

        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(DialogDefaults.DEFAULT_Padding))
                Text(
                    text = text,
                    color = app_textColor_333333,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
//                        .background(China.r_fen_hong)
                        .weight(1f)
                        .padding(horizontal = DialogDefaults.DEFAULT_Padding)
                        .wrapContentSize()
                )
                Spacer(modifier = Modifier.height(12.dp))
                DividerBlock()
                Box(modifier = Modifier.height(IntrinsicSize.Min)) {

                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(vertical = DialogDefaults.DEFAULT_Padding)
                            .clickableX { onDismissRequest() }, text = "关闭"
                    )

                    /* CHPressedView(content = { isPressed ->
                         val scale = if (isPressed) 1.2f else 1f
                         val alpha = if (isPressed) .5f else 1f
                         Text(text = "关闭",
                             fontSize = 14.sp,
                             textAlign = TextAlign.Center,
                             fontWeight = FontWeight.Bold,
                             modifier = Modifier
                                 .fillMaxSize()
                                 .alpha(alpha)
                                 .scale(scale)
 //                            .fillMaxWidth()
 //                            .wrapContentHeight()
 ////                        .clip(RoundedCornerShape(12.dp))
 //                            .clickable {
 //                                onDismissRequest()
 //                            }
 //                            .padding(vertical = DialogDefaults.DEFAULT_Padding)
                         )
                     }, onClick = onDismissRequest,    modifier = Modifier
                         .fillMaxWidth()
                         .wrapContentHeight()
                         .padding(vertical = DialogDefaults.DEFAULT_Padding))*/
                }

            }
        }
    }
}