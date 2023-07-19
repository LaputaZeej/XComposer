package com.yunext.twins.ui.page

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yunext.twins.ui.compoents.China
import com.yunext.twins.ui.theme.app_textColor_999999

@Composable
fun TwinsVersion(modifier:Modifier,version: String) {
    Text(
        version,
        color = app_textColor_999999.copy(.5f),
        fontSize = 11.sp,
        fontWeight = FontWeight.Light,
        modifier = modifier.wrapContentSize().padding(16.dp)
    )
}