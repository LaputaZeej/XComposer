package com.yunext.twins.ui.page.device

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yunext.twins.data.ItemDefaults
import com.yunext.twins.ui.theme.app_appColor
import com.yunext.twins.ui.theme.app_blue_light
import com.yunext.twins.ui.theme.app_textColor_333333
import com.yunext.twins.ui.theme.app_textColor_999999
import com.yunext.twins.util.randomText

@Composable
internal fun <T> StructItemList(list: List<T>) {

    LazyColumn(modifier = Modifier.heightIn(max = ItemDefaults.contentValueMaxHeight)) {
        itemsIndexed(list, key = { _, data ->
            data.toString()
        }) { index, data ->
            StructItem(data)
            if (index < list.size) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(.5.dp)
                        .background(ItemDefaults.contentBorderColor)
                )
            }
        }

    }
}

@Composable
private fun <T> StructItem(data: T) {
    val type by remember {
        mutableStateOf(ItemDefaults.valueTypes.random())
    }

    val name by remember {
        mutableStateOf(randomText())
    }

    val key by remember {
        mutableStateOf(randomText())
    }

    val value by remember {
        mutableStateOf(randomText())
    }

    Row(
        Modifier
            .fillMaxWidth()
            .height(40.dp)
            .padding(start = 17.dp, end = 26.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            Text(text = name, fontSize = 16.sp,color= app_textColor_333333, fontWeight = FontWeight.Bold)
            Text(text = key, fontSize = 11.sp,color= app_textColor_999999, fontWeight = FontWeight.Normal)
        }
        Spacer(modifier = Modifier.width(55.dp))
        Text(
            text = value,
            fontSize = 16.sp,
            color = app_textColor_333333,
            fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(16.dp))
        LabelPart(type, app_appColor, app_blue_light)
    }
}

@Composable
internal fun LabelPart(label: String, fontColor: Color, background: Color) {
    Text(
        text = label,
        fontSize = 11.sp,
        color = fontColor,
        modifier = Modifier

            .background(color = background, shape = ItemDefaults.labelShape)
            .padding(horizontal = 8.dp, vertical = 3.dp)
    )
}

@Composable
internal fun BottomPart(desc: String) {
    if (desc.isNotEmpty()) {
        Text(
            text = desc,
            fontSize = 12.sp,
            color = app_textColor_999999
        )
    }

}

