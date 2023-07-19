package com.yunext.twins.ui.page.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.yunext.twins.ui.compoents.TwinsTitle

@Composable
fun TwinsSettingPage(onLeft:()->Unit) {
    Column {
        TwinsTitle(text = "TwinsSettingPage", leftClick = {
            onLeft()
        })
        Text("TwinsSettingPage")
    }
}