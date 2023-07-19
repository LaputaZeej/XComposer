package com.yunext.twins.ui.page.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yunext.twins.data.DeviceAndState
import com.yunext.twins.ui.compoents.CHItemShadowShape

@Composable
actual fun TwinsDeviceItem(modifier: Modifier, device: DeviceAndState, onClick: () -> Unit) {

    TwinsDeviceItemCommon(modifier, device,onClick)
}

private const val MAX_CELLS = 3

@Composable
actual fun TwinsDeviceList(
    modifier: Modifier,
    list: List<DeviceAndState>,
    onDeviceSelected: (DeviceAndState) -> Unit,
) {
//    LazyVerticalGrid(
//        modifier = Modifier.padding(0.dp),
//        columns = GridCells.Fixed(3),
//        contentPadding = PaddingValues(8.dp),
//        verticalArrangement = Arrangement.spacedBy(8.dp),
//        horizontalArrangement = Arrangement.spacedBy(8.dp)
//    ) {
//        items(items = list, key = { it.communicationId }) { device ->
//            CHItemShadowShape {
//                TwinsDeviceItem(modifier = Modifier.fillMaxWidth(), device = device) {
//                    onDeviceSelected.invoke(device)
//                }
//            }
//        }


    LazyColumn(
        modifier = Modifier.padding(0.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        gridItemsSpace(modifier, list, key = {
            list[it].communicationId
        }, columnCount = MAX_CELLS) { device ->
            CHItemShadowShape {
                TwinsDeviceItem(modifier = Modifier.fillMaxWidth(), device = device) {
                    onDeviceSelected.invoke(device)
                }
            }
        }
    }
}

fun <T> LazyListScope.gridItemsSpace(
    modifier: Modifier,
    data: List<T>,
    key: ((index: Int) -> Any)? = null,
    columnCount: Int,
    horizontal: Arrangement.Horizontal = Arrangement.Start,
    content: @Composable (T) -> Unit,

    ) {
    val size = data.size
    val fullColumnCount = size / columnCount // 满格的
    val lastFullColumnCount = size % columnCount // 最后一行
    val horizontalSpacer = @Composable {
        Spacer(modifier = Modifier.width(16.dp))
    }

    items(fullColumnCount, key = key) { fullIndex ->
        Row(
            modifier = modifier.padding(horizontal = 8.dp).wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = horizontal
        ) {
            (0 until MAX_CELLS).forEach { rowIndex ->
                val device = data[fullIndex * MAX_CELLS + rowIndex]
                if(rowIndex!=0){
                    horizontalSpacer()
                }

                Box(
                    modifier = Modifier.weight(1f, fill = true),
                    propagateMinConstraints = true
                ) {
                    content(device)
                }
            }
        }
    }
    if (lastFullColumnCount > 0) {
        item(lastFullColumnCount) {
            Row(
                modifier = modifier.padding(horizontal = 8.dp).wrapContentHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = horizontal
            ) {
                (0 until lastFullColumnCount).forEach {
                    val device = data[fullColumnCount * MAX_CELLS + it]
                    if(it!=0){
                        horizontalSpacer()
                    }
                    Box(
                        modifier = Modifier.weight(1f, fill = true),
                        propagateMinConstraints = true
                    ) {
                        content(device)
                    }
                }
            }
        }
    }

}

fun <T> LazyListScope.gridItems(
    modifier: Modifier,
    data: List<T>,
    key: ((index: Int) -> Any)? = null,
    columnCount: Int,
    horizontal: Arrangement.Horizontal = Arrangement.Start,
    content: @Composable BoxScope.(T) -> Unit,

    ) {
    val size = data.size
    val rows = if (size == 0) 0 else 1 + (size - 1) / columnCount
    items(rows, key = key) { rowIndex ->
        Row(
            modifier = modifier.padding(horizontal = 8.dp).wrapContentHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = horizontal
        ) {
            for (columnIndex in 0 until columnCount) {
                val itemIndex = rowIndex * columnCount + columnIndex
                if (itemIndex < size) {
                    if (itemIndex < 0) return@items
                    Box(
                        modifier = Modifier.weight(1f, fill = true),
                        propagateMinConstraints = true
                    ) {
                        content(data[itemIndex])
                    }
                } else {
                    Spacer(Modifier.weight(1f, fill = true))
                }
            }
        }
    }
}

