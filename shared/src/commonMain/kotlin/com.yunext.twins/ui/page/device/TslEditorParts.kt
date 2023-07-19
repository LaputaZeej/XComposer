package com.yunext.twins.ui.page.device

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yunext.twins.data.ItemDefaults
import com.yunext.twins.ui.theme.app_textColor_666666


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import com.yunext.twins.ui.compoents.CHPressedView
import com.yunext.twins.ui.compoents.China
import com.yunext.twins.ui.compoents.CommitButtonBlock
import com.yunext.twins.ui.compoents.DividerBlock
import com.yunext.twins.ui.compoents.EditTextBlock
import com.yunext.twins.ui.compoents.EditTextCenterBlock
import com.yunext.twins.ui.compoents.clickableX
import com.yunext.twins.ui.compoents.CHItemShadowShape
import com.yunext.twins.ui.theme.Twins
import com.yunext.twins.ui.theme.app_appColor
import com.yunext.twins.ui.theme.app_gray
import com.yunext.twins.ui.theme.app_gray_f4f5f7
import com.yunext.twins.ui.theme.app_red
import com.yunext.twins.ui.theme.app_textColor_333333
import com.yunext.twins.ui.theme.app_textColor_999999
import com.yunext.twins.util.randomText
import kotlin.jvm.JvmStatic

@Composable
fun PropertyBottomSheet(
    data: Any,
    add: Pair<Boolean, (Any) -> Unit>,
    onClose: () -> Unit,
    onCommitted: (Any) -> Unit,
) {
    val any = data ?: ""
    val type: TslPropertyTypeVo by remember {
        mutableStateOf(TslPropertyTypeVo.STRUCT)
    }
//    val type = if (add.first) TslPropertyTypeVo.TEXT else TslPropertyTypeVo.ARRAY


    XPopContainer(Alignment.BottomCenter) {
        when (type) {
            TslPropertyTypeVo.INT -> {
                NumberPropertyBottomSheet(any, onClose = onClose, onCommitted = {
                    onCommitted.invoke(it)
                })
            }

            TslPropertyTypeVo.FLOAT -> {
                NumberPropertyBottomSheet(any, onClose = onClose, onCommitted = {
                    onCommitted.invoke(it)
                })
            }

            TslPropertyTypeVo.DOUBLE -> {
                NumberPropertyBottomSheet(any, onClose = onClose, onCommitted = {
                    onCommitted.invoke(it)
                })
            }

            TslPropertyTypeVo.TEXT -> {
                TextPropertyBottomSheet(any, onClose = onClose, onCommitted = {
                    onCommitted.invoke(it)
                })
            }

            TslPropertyTypeVo.DATE -> {
                TextPropertyBottomSheet(any, onClose = onClose, onCommitted = {
                    onCommitted.invoke(it)
                })
            }

            TslPropertyTypeVo.BOOL -> {
                EnumPropertyBottomSheet(any, onClose = onClose, onSelected = {
                    onCommitted.invoke(it)
                })
            }

            TslPropertyTypeVo.ENUM -> {
                EnumPropertyBottomSheet(any, onClose = onClose, onSelected = {
                    onCommitted.invoke(it)
                })
            }

            TslPropertyTypeVo.STRUCT -> {
                StructPropertyBottomSheet(any, onClose = onClose, onCommitted = {
                    onCommitted.invoke(it)
                })
            }

            TslPropertyTypeVo.ARRAY -> {
                ArrayPropertyBottomSheet(any, onClose = onClose, onCommitted = {
                    onCommitted.invoke(it)
                }, onAdd = {
                    add.second.invoke(any)
                })
            }
        }
    }
}


// <editor-fold desc="[修改struct]">
// struct a
@Composable
private fun <T : Any> StructPropertyBottomSheet(
    property: T,
    onClose: () -> Unit,
    onCommitted: (Any) -> Unit,
) {
    val title = randomText()
    val key = randomText(6)
    val spec = "取之范围1-33131${randomText(4)}"
    var list: List<Any> by remember {
        mutableStateOf(List(20) { it to it })
    }
    TslEditor(title, key, onClose, onCommit = {
        onCommitted.invoke(list)
    }) {

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp)
        ) {
            itemsIndexed(items = list, key = { _, it -> it.toString() }) { index, it ->
                StructItem(index, it) {
                    //list=list-list[index]+it

                }
            }
        }
        DividerBlock(color = app_gray)
        Text(
            text = spec,
            color = app_textColor_999999,
            fontSize = 13.sp,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth()
                .padding(vertical = 12.dp)
        )
    }
}

@Composable
private fun StructItem(index: Int, value: Any, onValueChanged: (Any) -> Unit) {
    val key = randomText(6)
    val type by remember {
        mutableStateOf(( TslPropertyTypeVo.values().toList() -TslPropertyTypeVo.STRUCT-TslPropertyTypeVo.ARRAY).random())
//        mutableStateOf(TslPropertyTypeVo.INT)
    }

    when (type) {
        TslPropertyTypeVo.INT -> {
            var intValue: Int by remember {
                mutableStateOf(0)
            }
            StructItemForNumber(key, intValue, "h") {
                intValue = it
                onValueChanged.invoke(it)
            }
        }

        TslPropertyTypeVo.FLOAT -> {
            var floatValue by remember {
                mutableStateOf(0f)
            }
            StructItemForNumber(key, floatValue, "h") {
                floatValue = it
                onValueChanged.invoke(it)
            }
        }

        TslPropertyTypeVo.DOUBLE -> {
            var doubleValue by remember {
                mutableStateOf(0.0)
            }
            StructItemForNumber(key, doubleValue, "h") {
                doubleValue = it
                onValueChanged.invoke(it)
            }
        }

        TslPropertyTypeVo.TEXT -> {
            var textValue by remember {
                mutableStateOf("")
            }
            StructItemForText(key, textValue) {
                textValue = it
                onValueChanged.invoke(it)
            }
        }

        TslPropertyTypeVo.DATE -> {
            var dateValue by remember {
                mutableStateOf("")
            }
            StructItemForText(key, dateValue) {
                dateValue = it
                onValueChanged.invoke(it)
            }
        }

        TslPropertyTypeVo.BOOL -> {
            var v by remember {
                mutableStateOf("")
            }
            StructItemForEnum(key+"bool", v) {
                v = it
                onValueChanged.invoke(it)
            }
        }
        TslPropertyTypeVo.ENUM -> {
            var v by remember {
                mutableStateOf("")
            }
            StructItemForEnum(key, v) {
                v = it
                onValueChanged.invoke(it)
            }
        }
        TslPropertyTypeVo.STRUCT -> throw TslPropertyTypeVo.Struct_Not_Support_Struct_Exception
        TslPropertyTypeVo.ARRAY -> throw TslPropertyTypeVo.Struct_Not_Support_Array_Exception
    }
}

internal fun Number.isZero() = when(this){
    is Float-> (this ==0f)
    is Double-> (this ==0.0)
    is Int-> (this ==0)
    is Long-> (this ==0L)
    is Byte-> (this == 0.toByte())
    is Short-> (this == 0.toShort())
    else -> false
}

@Composable
private inline fun <reified T : Number> StructItemForNumber2(
    key: String,
    value: T,
    unit: String,
    crossinline onValueChanged: (T) -> Unit,
) {
    val keyboardType = when (value::class) {
        Float::class -> KeyboardType.Decimal
        Int::class -> KeyboardType.Number
        Double::class -> KeyboardType.Decimal
        Long::class -> KeyboardType.Decimal
        else -> throw Throwable("错误的value $value")
    }
    Row(
        Modifier
            .fillMaxWidth()
            .height(48.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Text(key, style = Twins.twins_title)
        Spacer(modifier = Modifier.width(8.dp))
        EditTextBlock(
            modifier = Modifier
                .weight(1f),
//                .background(China.g_zhu_lv),
            text = "$value",
            hint = "请输入",
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType)
        ) { text ->
            try {
                val result = when (value) {
                    Float::class -> text.toFloat()
                    Int::class -> text.toInt()
                    Double::class -> text.toDouble()
                    Long::class -> text.toLong()
                    else -> throw Throwable("错误的value $value")
                } as T
                onValueChanged.invoke(result)
            } catch (e: Throwable) {
                e.printStackTrace()
            }

        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = unit, style = Twins.twins_title.copy(color = app_textColor_999999))
    }
}

@Composable
private  fun < T : Number> StructItemForNumber(
    key: String,
    value: T,
    unit: String,
     onValueChanged: (T) -> Unit,
) {
    val keyboardType = when (value) {
        is Float -> KeyboardType.Decimal
        is Int -> KeyboardType.Number
        is Double -> KeyboardType.Decimal
        else -> throw Throwable("错误的value $value")
    }
    Row(
        Modifier
            .fillMaxWidth()
            .height(48.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Text(key, style = Twins.twins_title)
        Spacer(modifier = Modifier.width(8.dp))
        EditTextBlock(
            modifier = Modifier
                .weight(1f),
//                .background(China.g_zhu_lv),
            text = if(value.isZero()) "" else "$value",
            hint = "$value",
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType)
        ) { text ->
            try {
                val result = when (value) {
                    is Float -> text.toFloat()
                    is Int -> text.toInt()
                    is Double -> text.toDouble()
                    else -> throw Throwable("错误的value $value")
                } as T
                onValueChanged.invoke(result)
            } catch (e: Throwable) {
                e.printStackTrace()
            }

        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = unit, style = Twins.twins_title.copy(color = app_textColor_999999))
    }
}

@Composable
private fun StructItemForText(key: String, value: String, onValueChanged: (String) -> Unit) {
    Row(
        Modifier
            .fillMaxWidth()
            .height(48.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Text(key, style = Twins.twins_title)
        Spacer(modifier = Modifier.width(8.dp))
        EditTextBlock(
            modifier = Modifier
                .weight(1f),
//                .background(China.g_zhu_lv),
            text = value,
            hint = "请输入",
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
        ) { text ->
            onValueChanged.invoke(text)
        }
    }
}

@Composable
private fun <T> StructItemForEnum(key: String, value: T, onValueChanged: (T) -> Unit) {
    val list: List<Int> by remember {
        mutableStateOf(List(4) { it })
    }
    var selected: Int by remember {
        mutableStateOf(list[0])
    }
    Column(
        Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(key, style = Twins.twins_title)
        Spacer(modifier = Modifier.height(12.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)){
            itemsIndexed(list, { _, it -> it.toString() }) { index, it ->
                EnumItem(index, it, selected == index) {
                    selected = index
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}


// struct z
// </editor-fold>

// <editor-fold desc="[修改array]">
// 修改array a
@Composable
private fun <T : Any> ArrayPropertyBottomSheet(
    property: T,
    onClose: () -> Unit,
    onAdd: () -> Unit,
    onCommitted: (Any) -> Unit,
) {
    val title = randomText(4)
    val key = randomText(6)
    val spec = "取之范围1-33131${randomText(4)}"
    var list: List<Any> by remember {
        mutableStateOf(List(20) { it })
    }
    TslEditor(title, key, onClose, onCommit = {
        onCommitted.invoke(list)
    }) {

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp)
        ) {
            itemsIndexed(items = list, key = { _, it -> it.toString() }) { index, it ->
                ArrayItem(index, it) {
                    list = list - it
                }
            }
        }
        Spacer(modifier = Modifier.heightIn(12.dp))
        CHPressedView(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(), content = { isPressed ->
            val alpha = if (isPressed) .5f else 1f
            Text(
                text = "添加",
                color = app_appColor.copy(alpha = alpha),
                fontSize = 13.sp,
                modifier = Modifier


//                    .wrapContentWidth()
//                    .wrapContentHeight()
                    .border(
                        1.dp, app_appColor.copy(alpha = alpha), RoundedCornerShape(4.dp)
                    )
                    .fillMaxWidth()
                    .padding(vertical = 11.dp),
                textAlign = TextAlign.Center
            )
        }, onClick = {
            onAdd()
        })

        DividerBlock(color = app_gray)
        Text(
            text = spec,
            color = app_textColor_999999,
            fontSize = 13.sp,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth()
                .padding(vertical = 12.dp)
        )
    }
}

@Composable
private fun ArrayItem(index: Int, any: Any, onDelete: () -> Unit) {
    val text = randomText(6)
    Row(
        Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Text(
            text = text,
            color = app_textColor_333333,
            fontSize = 14.sp,
            modifier = Modifier

                .weight(1f)
        )
        Spacer(modifier = Modifier.width(32.dp))
        CHPressedView(modifier = Modifier, content = { isPressed ->
            val alpha = if (isPressed) .5f else 1f
            Text(
                text = "删除",
                color = app_red.copy(alpha = alpha),
                fontSize = 13.sp,
                modifier = Modifier


//                    .wrapContentWidth()
//                    .wrapContentHeight()
                    .border(
                        1.dp, app_red.copy(alpha = alpha), RoundedCornerShape(4.dp)
                    )
                    .padding(vertical = 6.dp, horizontal = 8.dp),
                textAlign = TextAlign.Center
            )
        }, onClick = {
            onDelete()
        })

    }
}
// 修改array z
// </editor-fold>

// <editor-fold desc="[修改text,date]">
// 修改text,date a
@Composable
private fun <T : Any> TextPropertyBottomSheet(
    property: T,
    onClose: () -> Unit,
    onCommitted: (Any) -> Unit,
) {
    val title = ItemDefaults.randomTextInternal(4)
    val key = ItemDefaults.randomTextInternal(6)
    val spec = "取之范围1-33131${ItemDefaults.randomTextInternal(4)}"
    var value: String by remember {
        mutableStateOf("")
    }
    TslEditor(title, key, onClose, onCommit = {
        onCommitted.invoke(value)
    }) {
        EditTextCenterBlock(
            text = "$value",
            hint = "请输入",
            onValueChange = {
                value = it
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
        )
        DividerBlock(color = app_gray)
        Text(
            text = spec,
            color = app_textColor_999999,
            fontSize = 13.sp,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth()
                .padding(vertical = 12.dp)
        )
    }
}
// 修改text,date z
// </editor-fold>

// <editor-fold desc="[修改int float double]">
// 修改int float double a
@Composable
private fun <T : Any> NumberPropertyBottomSheet(
    property: T,
    onClose: () -> Unit,
    onCommitted: (Any) -> Unit,
) {
    val title = ItemDefaults.randomTextInternal(4)
    val key = ItemDefaults.randomTextInternal(6)
    val spec = "取之范围1-33131${ItemDefaults.randomTextInternal(4)}"
    val unit = "km"
    var value: Number by remember {
        mutableStateOf(0)
    }
    TslEditor(title, key, onClose, onCommit = {
        onCommitted.invoke(value)
    }) {
//        Row(verticalAlignment = Alignment.CenterVertically) {
//            EditTextCenterBlock(text = "$value", hint = "请输入", onValueChange = {
//                try {
//                    value = it.toDouble()
//                } catch (e: Throwable) {
//                    e.printStackTrace()
//                }
//            }, modifier = Modifier.weight(1f))
//            Spacer(modifier = Modifier.width(4.dp))
//            Text(text = unit, color = app_textColor_999999, fontSize = 11.sp)
//        }
        Row  (modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            EditTextCenterBlock(text = "$value", hint = "请输入", onValueChange = {
                try {
                    value = it.toDouble()
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }, modifier = Modifier
                .weight(1f,true)
                .widthIn(max = 200.dp))
            Text(
                text = unit,
                color = app_textColor_999999,
                fontSize = 11.sp,
                modifier = Modifier)
        }
        DividerBlock(color = app_gray)
        Text(
            text = spec,
            color = app_textColor_999999,
            fontSize = 13.sp,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth()
                .padding(vertical = 12.dp)
        )
    }
}
// 修改int float double z
// </editor-fold>

// <editor-fold desc="[修改enum]">
// enum a
@Composable
private fun <T : Any> EnumPropertyBottomSheet(
    property: T,
    onClose: () -> Unit,
    onSelected: (Any) -> Unit,
) {
    val title = property.toString()
    val desc = property.toString()
    val list: List<Int> by remember {
        mutableStateOf(List(4) { it })
    }
    var selected: Int by remember {
        mutableStateOf(list[0])
    }
    TslEditor(title, desc, onClose, onCommit = {
        onSelected.invoke(selected)
    }) {
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            itemsIndexed(list, { _, it -> it.toString() }) { index, it ->
                EnumItem(index, it, selected == index) {
                    selected = index
                }
            }
        }
    }
}

private val enumShape = RoundedCornerShape(22.dp)

@Composable
private fun EnumItem(index: Int, any: Any, selected: Boolean, onSelected: () -> Unit) {
    val textColor = if (selected) app_appColor else app_textColor_333333
    fun Modifier.selected(): Modifier {
        return if (selected) {
            this
                .clip(enumShape)
                .border(1.dp, textColor, enumShape)
                .clickable {
                    onSelected()
                }
                .padding(horizontal = 16.dp, vertical = 8.dp)
        } else {
            this
                .clip(enumShape)
                .background(app_gray_f4f5f7)
                .clickable {
                    onSelected()
                }
                .padding(horizontal = 16.dp, vertical = 8.dp)
        }
    }

    Text(
        text = "$index - 上",
        fontSize = 14.sp,
        color = textColor,
        modifier = Modifier.selected()
    )
}
// enum z
// </editor-fold>

private enum class TslPropertyTypeVo(val text: String) {

    INT("int"),
    FLOAT("float"),
    DOUBLE("double"),
    TEXT("text"),
    DATE("date"),
    BOOL("bool"),
    ENUM("enum"),
    STRUCT("struct"),
    ARRAY("array"),
    ;

    companion object {
        @JvmStatic
        internal fun from(text: String): TslPropertyTypeVo {
            return when (text) {
                INT.text -> INT
                FLOAT.text -> FLOAT
                DOUBLE.text -> DOUBLE
                TEXT.text -> TEXT
                DATE.text -> DATE
                BOOL.text -> BOOL
                ENUM.text -> ENUM
                STRUCT.text -> STRUCT
                ARRAY.text -> ARRAY
                else -> throw IllegalStateException("不支持的TslUIType:$text")
            }
        }

        internal val Struct_Not_Support_Struct_Exception = IllegalStateException("struct not support struct value .")
        internal val Struct_Not_Support_Array_Exception = IllegalStateException("struct not support array value .")
    }
}

// <editor-fold desc="[base]">
@Composable
private fun TslEditor(
    title: String,
    desc: String,
    onClose: () -> Unit,
    onCommit: () -> Unit,
    content: @Composable () -> Unit,
) {
    CHItemShadowShape(elevation = 32.dp) {
        Column(
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .background(China.w_xue_bai)
                .padding(horizontal = 24.dp, vertical = 14.dp)
        ) {
            Box(Modifier.fillMaxWidth()) {
                Text(
                    text = title,
                    fontSize = 18.sp,
                    color = app_textColor_333333,
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                )

                Icon(imageVector = Icons.Filled.Close, contentDescription = null,
                    Modifier
                        .size(24.dp)
                        .clickableX {
                            onClose()
                        }
                        .align(Alignment.CenterEnd))


            }
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = desc,
                fontSize = 14.sp,
                color = app_textColor_333333,
                modifier = Modifier.align(
                    Alignment.CenterHorizontally
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            content()
            Spacer(modifier = Modifier.height(48.dp))
            CommitButtonBlock(
                text = "确定",
                enable = true
            ) {
                onCommit.invoke()
            }
            Spacer(modifier = Modifier.height(12.dp))

        }
    }

}
// </editor-fold>


@Composable
internal fun InputPart(label: String, list: List<*>) {
    Column(modifier = ItemDefaults.borderModifier.padding(12.dp)) {
        Text(text = label, color = app_textColor_666666, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = ItemDefaults.border4Modifier) {
            StructItemList(list)
        }
    }
}