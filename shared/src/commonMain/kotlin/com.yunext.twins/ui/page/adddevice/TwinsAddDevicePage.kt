package com.yunext.twins.ui.page.adddevice

import LocalPaddingValues
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yunext.twins.base.TODO
import com.yunext.twins.data.DeviceType
import com.yunext.twins.ui.compoents.CheckedTextBlock
import com.yunext.twins.ui.compoents.CommitButtonBlock
import com.yunext.twins.ui.compoents.EditTextBlock
import com.yunext.twins.ui.compoents.TwinsBackgroundBlock
import com.yunext.twins.ui.compoents.TwinsTitle
import com.yunext.twins.ui.theme.Twins

@Composable
fun TwinsAddDevicePage(
    onLeft: () -> Unit,
    onDeviceCommit: (deviceName: String, deviceType: DeviceType, deviceCommunicationId: String, deviceModel: String) -> Unit,
) {
    var deviceName: String by rememberSaveable(Unit) {
        mutableStateOf("")
    }
    var deviceType: DeviceType by rememberSaveable(Unit) {
        mutableStateOf(DeviceType.WIFI)
    }
    var deviceCommunicationId: String by rememberSaveable(Unit) {
        mutableStateOf("")
    }
    var deviceModel: String by rememberSaveable(Unit) {
        mutableStateOf("")
    }

    TwinsBackgroundBlock(grey = true)

    Column(
        modifier =Modifier.fillMaxSize()
    ) {
        TwinsTitle(modifier = Modifier.background(Color.White)
            ,text = "新增设备", leftClick = {
            onLeft()
        })
        Spacer(modifier = Modifier.height(16.dp))

        /* 设备名称 */
        SubItem(modifier = Modifier.styleInternal(),
            title = {
                SubItemTitle("设备名称", modifier = Modifier)
            },
            value = {
                EditTextBlock(
                    text = deviceName,
                    hint = "请输入设备名称",
                    onValueChange = {
                        deviceName = it
                    }, modifier = Modifier.fillMaxSize()
                )
            })
        LineBlock()
        /*ConstraintLayout(
            modifier = Modifier
                .styleInternal(), constraintSet = ConstraintSet {
                val deviceNameRef = createRefFor(Key_Device_Name)
                val deviceNameValueRef = createRefFor(Key_Device_Name_Value)
                constrain(deviceNameRef) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(deviceNameValueRef.start)
                }

                constrain(deviceNameValueRef) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(deviceNameRef.end)
                    width = Dimension.fillToConstraints
                }
            }
        ) {
            SubItemTitle("设备名称", Modifier.layoutId(Key_Device_Name))
            EditTextBlock(text = deviceName, hint = "请输入设备名称", onValueChange = {
                deviceName = it
            }, modifier = Modifier
                .layoutId(Key_Device_Name_Value)
                .fillMaxSize())
        }
        LineBlock()*/
        /* 通信类型 */
        SubItem(
            modifier = Modifier.styleInternal(),
            title = { SubItemTitle("通信类型", Modifier) }) {

            Row(modifier = Modifier.fillMaxSize(), horizontalArrangement = Arrangement.End) {
//            Checkbox(checked = selDeviceType == DeviceType.WIFI, onCheckedChange = {
//                selDeviceType = if (it) DeviceType.WIFI else selDeviceType
//            })
//
//
//            Checkbox(checked = selDeviceType == DeviceType.GPRS, onCheckedChange = {
//                selDeviceType = if (it) DeviceType.GPRS else selDeviceType
//            })

                CheckedTextBlock(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = "WiFi/Bluetooth",
                    deviceType == DeviceType.WIFI
                ) {
                    if (it && deviceType != DeviceType.WIFI) {
                        deviceType = DeviceType.WIFI
                    }
                }

                CheckedTextBlock(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = "4G",
                    deviceType == DeviceType.GPRS
                ) {
                    if (it && deviceType != DeviceType.GPRS) {
                        deviceType = DeviceType.GPRS
                    }
                }
            }
        }
        LineBlock()
        /* 通信ID */
        SubItem(
            modifier = Modifier.styleInternal(),
            title = { SubItemTitle("通信ID", Modifier) },
            value = {
                EditTextBlock(
                    text = deviceCommunicationId,
                    hint = "自动填充可修改",
                    onValueChange = {
                        deviceCommunicationId = it
                    }, modifier = Modifier.fillMaxSize()
                )
            })
        LineBlock()
        /* 设备型号 */
        SubItem(
            modifier = Modifier.styleInternal(),
            title = { SubItemTitle("设备型号", Modifier) },
            value = {
                EditTextBlock(
                    text = deviceModel,
                    hint = "请输入设备型号",
                    onValueChange = {
                        deviceModel = it
                    }, modifier = Modifier.fillMaxSize()
                )
            })

        Spacer(modifier = Modifier.height(32.dp))
        Box(modifier = Modifier.padding(16.dp)) {
            CommitButtonBlock(
                text = "新增",
                enable = deviceModel.isNotEmpty() && deviceName.isNotEmpty() && deviceCommunicationId.isNotEmpty()
            ) {
                onDeviceCommit(deviceName, deviceType, deviceCommunicationId, deviceModel)
            }
        }
    }

    @TODO("loading dialog")
    val a = 1
//    if (loading) {
//        CHLoadingDialog(dimAmount = 0.1f) {
//
//        }
//    }
}

private val LineBlock: @Composable () -> Unit = { Spacer(modifier = Modifier.height(1.dp)) }
private fun Modifier.styleInternal() = height(48.dp)
    .fillMaxWidth()
    .background(Color.White)
    .padding(start = 16.dp, end = 16.dp)


@Composable
private fun SubItem(
    modifier: Modifier,
    title: @Composable () -> Unit,
    value: @Composable () -> Unit,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
        ) {
            title()
        }
        Box(
            modifier = Modifier.weight(1f)
        ) {
            value()
        }
    }
}

@Composable
private fun SubItemTitle(text: String, modifier: Modifier = Modifier) {
    androidx.compose.material.Text(text = text, style = Twins.twins_title, modifier = modifier)
}