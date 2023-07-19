package com.yunext.twins

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Preview
@Composable
fun DemoPreview() {
    Demo2()
}

@Composable
fun Demo2() {
    Text(
        "从入门到放弃",
        modifier = Modifier.size(200.dp).padding(16.dp).background(Color.LightGray),
        style = TextStyle.Default.copy(

        )
    )
}