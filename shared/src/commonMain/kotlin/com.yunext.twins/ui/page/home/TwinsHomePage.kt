package com.yunext.twins.ui.page.home

import LocalPaddingValues
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yunext.twins.base.TODO
import com.yunext.twins.base.UiState
import com.yunext.twins.base.processing
import com.yunext.twins.data.DeviceAndState
import com.yunext.twins.ui.compoents.TwinsBackgroundBlock
import com.yunext.twins.ui.compoents.TwinsEmptyViewForDevice
import com.yunext.twins.ui.compoents.dialog.CHAlertDialog
import com.yunext.twins.ui.compoents.dialog.CHLoadingDialog
import com.yunext.twins.ui.compoents.rememberOverscrollFlingBehavior
import com.yunext.twins.ui.page.TwinsVersion
import com.yunext.twins.ui.page.debug.NewsDialog
import com.yunext.twins.ui.theme.app_button_brush
import com.yunext.twins.ui.theme.app_textColor_999999


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TwinsHomePage(
    modifier: Modifier = Modifier,
    list: List<DeviceAndState>,
    uiState: UiState<*, *>,
    onRefresh: () -> Unit,
    onDeviceSelected: (DeviceAndState) -> Unit,
    onActionAdd: () -> Unit,
) {
    TwinsBackgroundBlock()

    val refreshing = uiState.processing()

    SideEffect {
        println("refreshing = $refreshing")
    }
    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing, onRefresh = { onRefresh() })
//    val scrollState = rememberScrollState()
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(LocalPaddingValues.current)
            .pullRefresh(pullRefreshState)
//            .verticalScroll(
//                scrollState,
//                flingBehavior = rememberOverscrollFlingBehavior { scrollState })
    ) {
        TwinsVersion(modifier = Modifier.align(Alignment.TopCenter), "HD孪生设备v1.0.0 $refreshing")

        Column(


        ) {
            Spacer(modifier = Modifier.height(36.dp))
            @TODO("app name")
            Text(

                text = "KMM孪生设备",//stringResource(id = R.string.app_name),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                // fontStyle = MaterialTheme.typography.titleLarge
            )
            Text(
                text = "ssl://emqtt-test.yunext.com:8881",//stringResource(id = R.string.app_virtual_device),
                fontSize = 11.sp,
                fontWeight = FontWeight.Normal,
                style = MaterialTheme.typography.titleSmall.copy(app_textColor_999999),
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                // fontStyle = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            @TODO("app name")
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "虚拟设备",//stringResource(id = R.string.app_virtual_device),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 16.dp, end = 8.dp)
                    // fontStyle = MaterialTheme.typography.titleLarge
                )
                Text(

                    text = "1",//stringResource(id = R.string.app_name),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 16.dp, end = 16.dp)
                    // fontStyle = MaterialTheme.typography.titleLarge
                )
            }

            Box(
                modifier = Modifier
                    .pullRefresh(pullRefreshState)
                    .weight(1f)
            ) {
                if (list.isEmpty()) {
                    TwinsEmptyViewForDevice()
                } else {
                    TwinsDeviceList(Modifier, list, onDeviceSelected)
                }



                PullRefreshIndicator(
                    refreshing = refreshing,
                    state = pullRefreshState,
                    scale = true,
                    modifier = Modifier.align(
                        Alignment.TopCenter
                    )
                )
            }
        }

        FloatingActionButton(
            onClick = {
                onActionAdd.invoke()
            },
            shape = CircleShape,
            elevation = FloatingActionButtonDefaults.elevation(4.dp),
            modifier = Modifier
                .padding(end = 16.dp, bottom = 16.dp)
                .align(Alignment.BottomEnd),
//            contentColor = Color.Red,
//            containerColor = China.r_luo_xia_hong

        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(58.dp)
                    .clip(CircleShape)
                    .background(brush = app_button_brush)
            ) {

                Text(
                    text = "+",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        var show by remember {
            mutableStateOf(false)
        }
        var index by remember {
            mutableStateOf(0)
        }
        Button(
            onClick = {
                index += 1
                show = !show
            },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(vertical = 16.dp)
        ) {
            Text(text = "Debug")
        }
        if (show) {
            if (index % 2 == 0) {
//                CHLoadingDialog(dimAmount = 0.1f) {
//                    show = false
//                }

                NewsDialog() {
                    show = false
                }
            } else {
                TODO("底部弹窗")
                CHAlertDialog("haha", "heheiee") {
                    show = false
                }
            }


        }


    }

}



