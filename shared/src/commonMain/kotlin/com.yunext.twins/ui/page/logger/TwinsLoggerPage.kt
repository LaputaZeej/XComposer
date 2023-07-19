package com.yunext.twins.ui.page.logger

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.yunext.twins.ui.compoents.TwinsTitle

@Composable
fun TwinsLoggerPage(onLeft: () -> Unit) {
    Column {
        TwinsTitle(text = "TwinsLoggerPage", leftClick = {
            onLeft()
        })

        Text("TwinsLoggerPage")
    }
}