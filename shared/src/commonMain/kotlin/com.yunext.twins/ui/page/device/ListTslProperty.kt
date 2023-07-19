package com.yunext.twins.ui.page.device

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yunext.twins.data.PropertyData

import com.yunext.twins.data.ItemDefaults
import com.yunext.twins.ui.compoents.CHItemShadowShape
import com.yunext.twins.ui.theme.app_appColor
import com.yunext.twins.ui.theme.app_blue_light
import com.yunext.twins.ui.theme.app_brush_item_content_spec
import com.yunext.twins.ui.theme.app_gray_light
import com.yunext.twins.ui.theme.app_orange
import com.yunext.twins.ui.theme.app_orange_light
import com.yunext.twins.ui.theme.app_red
import com.yunext.twins.ui.theme.app_red_light
import com.yunext.twins.ui.theme.app_textColor_333333
import com.yunext.twins.ui.theme.app_textColor_666666
import com.yunext.twins.ui.theme.app_textColor_999999
import com.yunext.twins.util.randomText
import kotlin.random.Random

// ----------------- properties ----------------- //

@Composable
fun <T> ListTslProperty(list: List<T>, onClick: (T) -> Unit) {
    val realList = list.map {
        PropertyData.random()
    }
    LazyColumn(
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(items = realList, key = { _, it ->
            it.toString()
        }) { index, it ->
            PropertyItem(it) {
                onClick(list[index])
            }
        }
    }
}

@Composable
private fun PropertyItem(data: PropertyData, onClick: () -> Unit) {
    val desc = data.desc
    CHItemShadowShape() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(ItemDefaults.itemShape)
                .background(ItemDefaults.itemBackground)
                .padding(16.dp)
        ) {
            // 头部基本信息
            HeaderPart(data = data) {
                onClick()
            }
            Spacer(modifier = Modifier.height(16.dp))
            // 内容
            ContentPart()
            Spacer(modifier = Modifier.height(16.dp))
            // desc
            BottomPart(desc)
        }
    }
}

@Composable
private fun HeaderPart(data: PropertyData, onClick: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(modifier = Modifier.weight(1f, true)) {

            Row(modifier = Modifier) {
                Text(
                    text = data.name.run { this.ifEmpty { "未知" } },
                    fontSize = 16.sp,
                    color = app_textColor_333333,
                    fontWeight = FontWeight.Bold
                )
                Text(text = data.key.run {
                    if (this.isEmpty()) "-" else "(${this})"
                }, fontSize = 16.sp, color = app_textColor_999999, fontWeight = FontWeight.Bold)

            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier) {
                val required = if (data.required) "必填" else "非必填"
                val requiredColor =
                    if (data.required) app_red to app_red_light else app_appColor to app_blue_light
                LabelPart(
                    required,
                    requiredColor.first,
                    requiredColor.second
                )
                Spacer(modifier = Modifier.width(8.dp))
                val label = when (data.readWrite) {
                    PropertyData.ReadWrite.R -> "只读"
                    PropertyData.ReadWrite.W -> "只写"
                    PropertyData.ReadWrite.RW -> "读写"
                }
                val labelColor = when (data.readWrite) {
                    PropertyData.ReadWrite.R -> app_orange to app_orange_light
                    PropertyData.ReadWrite.W -> app_orange to app_orange_light
                    PropertyData.ReadWrite.RW -> app_appColor to app_blue_light
                }
                LabelPart(label, labelColor.first, labelColor.second)
                Spacer(modifier = Modifier.width(8.dp))

                LabelPart(ItemDefaults.valueTypes.random(), app_appColor, app_blue_light)
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            modifier = Modifier
                .clip(ItemDefaults.editShape)
                .border(width = 1.dp, color = app_appColor, shape = ItemDefaults.editShape)
                .clickable {
                    onClick()
                }
                .padding(horizontal = 8.dp, vertical = 6.dp),
            text = "修改",
            fontSize = 13.sp,
            color = app_appColor
        )
    }
}

private enum class ValueType {
    TEXT, STRUCT, ARRAY;
}

@Composable
private fun ContentPart() {
    val valueType: ValueType = ValueType.values().random()

    Column(
        modifier = ItemDefaults.borderModifier
    ) {
        when (valueType) {
            ValueType.TEXT -> {
                val text = "值:text date float double int boolean enum"
                ContentPartValueNormal(text)
            }

            ValueType.STRUCT -> {
                ContentPartValueStruct()
            }

            ValueType.ARRAY -> {
                ContentPartValueArray()
            }
        }
        ContentPartSpec()
    }

}

@Composable
private fun ContentPartValue(value: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .clip(ItemDefaults.contentValueShape)
            .background(ItemDefaults.itemBackground)
            .padding(16.dp)
    ) {
        Text(text = "当前值", fontSize = 13.sp, color = app_textColor_666666)
        Spacer(modifier = Modifier.height(16.dp))
        value()

    }
}

@Composable
private fun ContentPartValueNormal(text: String) {
    ContentPartValue {
        Text(text = text, fontSize = 18.sp, color = app_textColor_333333)
    }
}

@Composable
private fun ContentPartValueArray() {
    val list = List(10) { it }
    ContentPartValue {
        Box(modifier = ItemDefaults.borderModifier) {
            ArrayItemList(list) {

            }
        }
    }
}

@Composable
private fun ContentPartValueStruct() {
    val list = List(10) { it }
    ContentPartValue {
        Box(modifier = ItemDefaults.borderModifier) {
            StructItemList(list)
        }
    }
}

@Composable
private fun <T> ArrayItemList(list: List<T>, onLook: (T) -> Unit) {
    LazyColumn(modifier = Modifier.heightIn(max = ItemDefaults.contentValueMaxHeight)) {
        itemsIndexed(list, key = { _, data ->
            data.toString()
        }) { index, data ->
            ArrayItem(data) {
                onLook.invoke(data)
            }
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
private fun <T> ArrayItem(data: T, onLook: () -> Unit) {
    val type = ItemDefaults.valueTypes.random()
    val isStruct = Random.nextBoolean()
    val value = randomText()
    Row(
        Modifier
            .fillMaxWidth()
            .height(35.dp)
            .run {
                if (isStruct) {
                    this.clickable {
                        onLook()
                    }
                } else this
            }
            .padding(start = 17.dp, end = 26.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IndexText(data.toString())
        Spacer(modifier = Modifier.width(55.dp))
        if (isStruct) {
            Text(
                text = "查看",
                fontSize = 16.sp,
                color = app_appColor,
                fontWeight = FontWeight.Bold,
                modifier = Modifier

                    .fillMaxHeight()
                    .wrapContentHeight()

                    .weight(1f),
            )
        } else {
            Text(
                text = value,
                fontSize = 16.sp,
                color = app_textColor_333333,
                fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))
        LabelPart(type, app_appColor, app_blue_light)
    }
}

@Composable
private fun IndexText(index: String) {
    Text(
        text = index,
        fontSize = 11.sp,
        color = app_textColor_999999,
        modifier = Modifier
            .size(14.dp)
            .clip(CircleShape)
            .border(.5.dp, color = app_gray_light, shape = CircleShape)
            .wrapContentHeight()
            .wrapContentWidth()
    )
}


@Composable
private fun ContentPartSpec() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(ItemDefaults.contentSpecShape)
            .background(app_brush_item_content_spec)
            .padding(16.dp)
    ) {
        Text(text = "约束", fontSize = 11.sp, color = app_textColor_666666)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "数据长度 0~255 个字节", fontSize = 13.sp, color = app_textColor_333333)
    }
}




