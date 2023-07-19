package com.yunext.twins.ui.compoents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.yunext.twins.ui.theme.ItemShape

@Composable
fun CHItemShadowShape(
    modifier: Modifier = Modifier,
    elevation: Dp = 8.dp,
    content: @Composable () -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .wrapContentSize()
//            .background(
//                shape = ItemShape,
//                color = Color.White
//            )
            .shadow(
                elevation = elevation,
                shape = ItemShape,
                clip = false,
//                ambientColor = China.b_tian_lan,
//                spotColor = China.r_fen_hong
            )
    ) {
        content()
    }
}