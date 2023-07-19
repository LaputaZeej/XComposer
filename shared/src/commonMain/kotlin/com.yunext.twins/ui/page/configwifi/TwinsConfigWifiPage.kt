package com.yunext.twins.ui.page.configwifi

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.yunext.twins.ui.compoents.TwinsTitle

@Composable
fun TwinsConfigWifiPage(onLeft:()->Unit,) {
    Column {
        TwinsTitle(text = "TwinsConfigWifiPage", leftClick = {
            onLeft()
        })
        Text("TwinsConfigWifiPage")
    }
}