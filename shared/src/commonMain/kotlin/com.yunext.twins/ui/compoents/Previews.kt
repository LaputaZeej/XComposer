package com.yunext.twins.ui.compoents//package com.yunext.twins.ui.compoents.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yunext.twins.ui.compoents.China

@Composable
fun PreviewPart(content: @Composable () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .background(China.w_qian_shi_bai)
            .padding(16.dp), contentAlignment = Alignment.Center
    ) {
        content()
    }
}

