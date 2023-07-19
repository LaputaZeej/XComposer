//package com.yunext.twins.ui.page.home
//
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.wrapContentHeight
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.layoutId
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.res.stringResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.constraintlayout.compose.ConstraintLayout
//import androidx.constraintlayout.compose.ConstraintSet
//import androidx.constraintlayout.compose.Dimension
//import com.yunext.twins.R
//import com.yunext.twins.data.DeviceAndState
//import com.yunext.twins.data.DeviceStatus
//import com.yunext.twins.data.ItemDefaults
//import com.yunext.twins.ui.compoents.LabelTextBlock
//import com.yunext.twins.ui.compoents.CHItemShadowShape
//import org.jetbrains.compose.resources.ExperimentalResourceApi
//
//
//@Composable
//internal fun CHDeviceItemPreview() {
//    Box(modifier = Modifier.padding(32.dp)) {
//        CHItemShadowShape() {
//            CHDeviceItem(DeviceAndState.DEBUG_ITEM) {
//
//            }
//        }
//    }
//}
//
//@OptIn(ExperimentalResourceApi::class)
//@Composable
//fun CHDeviceItem(
//    device: DeviceAndState,
//    modifier: Modifier = Modifier,
//    onClick: () -> Unit,
//) {
//    val constraints = ConstraintSet {
//        val deviceName = createRefFor("deviceName")
//        val deviceStatus = createRefFor("deviceStatus")
//        val deviceId = createRefFor("deviceId")
//        val deviceIdTitle = createRefFor("deviceIdTitle")
//        val deviceType = createRefFor("deviceType")
//        val deviceTypeTitle = createRefFor("deviceTypeTitle")
//
//        constrain(deviceName) {
//            top.linkTo(parent.top)
//            bottom.linkTo(deviceIdTitle.top)
//            start.linkTo(parent.start)
//            end.linkTo(deviceStatus.start, margin = 16.dp)
//            width = Dimension.fillToConstraints
//        }
//        constrain(deviceStatus) {
//            top.linkTo(deviceName.top)
//            bottom.linkTo(deviceName.bottom)
//            end.linkTo(parent.end)
//            start.linkTo(deviceName.end)
//        }
//
//        constrain(deviceIdTitle) {
//            top.linkTo(deviceName.bottom, margin = 16.dp)
//            bottom.linkTo(deviceTypeTitle.top)
//            start.linkTo(parent.start)
//        }
//
//        constrain(deviceTypeTitle) {
//            top.linkTo(deviceIdTitle.bottom, margin = 8.dp)
//            bottom.linkTo(parent.bottom)
//            start.linkTo(parent.start)
//        }
//
//
//        constrain(deviceId) {
//            top.linkTo(deviceIdTitle.top)
//            bottom.linkTo(deviceIdTitle.bottom)
//            start.linkTo(deviceIdTitle.end, margin = 4.dp)
//            end.linkTo(parent.end)
//            width = Dimension.fillToConstraints
//        }
//
//        constrain(deviceType) {
//            top.linkTo(deviceTypeTitle.top)
//            bottom.linkTo(deviceTypeTitle.bottom)
//            start.linkTo(deviceTypeTitle.end, margin = 4.dp)
//            end.linkTo(parent.end)
//            width = Dimension.fillToConstraints
//        }
//    }
//    ConstraintLayout(constraintSet = constraints, modifier = modifier
//        .fillMaxWidth()
//        .wrapContentHeight()
//        .clip(ItemDefaults.itemShape)
//        .background(Color.White)
//        .clickable {
//            onClick()
//        }
//        .padding(16.dp)
//    ) {
//        Text(
//            text = device.name,
//            modifier = Modifier.layoutId("deviceName"),
//            overflow = TextOverflow.Ellipsis,
//            maxLines = 1,
//            fontWeight = FontWeight.Bold,
//
//            )
//
//        ChDeviceStatus(deviceStatus = device.status, Modifier.layoutId("deviceStatus"))
//
//        LabelTextBlock(
//            text = stringResource(id = R.string.device_cid),
//            modifier = Modifier.layoutId("deviceIdTitle"), painter = painterResource(R.mipmap.icon_twins_label_bg)
//        )
//
//        LabelTextBlock(
//            text = stringResource(id = R.string.device_type),
//            modifier = Modifier.layoutId("deviceTypeTitle")
//        )
//
//        Text(
//            text = device.name,
//            Modifier.layoutId("deviceId"),
//            maxLines = 1,
//            fontSize = 11.sp,
//            color = Color(0xff666666),
//            overflow = TextOverflow.Ellipsis
//        )
//
//        Text(
//            text = device.name,
//            Modifier.layoutId("deviceType"),
//            maxLines = 1,
//            fontSize = 13.sp,
//            color = Color(0xff666666),
//            overflow = TextOverflow.Ellipsis
//        )
//    }
//}
//
//@ExperimentalResourceApi
//@Composable
//fun ChDeviceStatus(deviceStatus: DeviceStatus, modifier: Modifier = Modifier) {
//    when (deviceStatus) {
//        DeviceStatus.GPRSOffLine -> {
//            Image(
//                painter = painterResource(id = R.mipmap.icon_twins_offline),
//                contentDescription = "gprs_offline", modifier = modifier
//            )
//        }
//
//        DeviceStatus.GPRSOnLine -> {
//            Image(
//                painter = painterResource(id = R.mipmap.icon_twins_4g),
//                contentDescription = "wifi", modifier = modifier
//            )
//        }
//
//        DeviceStatus.WiFiOffLine -> {
//            Image(
//                painter = painterResource(id = R.mipmap.icon_twins_offline),
//                contentDescription = "wifi", modifier = modifier
//            )
//        }
//
//        DeviceStatus.WiFiOnLine -> {
//            Image(
//                painter = painterResource(id = R.mipmap.icon_twins_wifi),
//                contentDescription = "wifi", modifier = modifier
//            )
//        }
//    }
//}