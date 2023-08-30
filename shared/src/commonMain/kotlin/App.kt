import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.yunext.twins.base.Effect
import com.yunext.twins.data.DeviceAndState
import com.yunext.twins.data.DeviceStatus
import com.yunext.twins.data.DeviceType
import com.yunext.twins.data.ItemDefaults
import com.yunext.twins.ui.AddDeviceDestination
import com.yunext.twins.ui.AppDestination
import com.yunext.twins.ui.BleDestination
import com.yunext.twins.ui.DeviceDestination
import com.yunext.twins.ui.Empty
import com.yunext.twins.ui.HomeDestination
import com.yunext.twins.ui.LogDestination
import com.yunext.twins.ui.SettingDestination
import com.yunext.twins.ui.page.PageStateHolder
import com.yunext.twins.ui.page.adddevice.TwinsAddDevicePage
import com.yunext.twins.ui.page.configwifi.TwinsConfigWifiPage
import com.yunext.twins.ui.page.device.DeviceStateHolder
import com.yunext.twins.ui.page.device.MenuData
import com.yunext.twins.ui.page.device.TwinsDevicePage
import com.yunext.twins.ui.page.home.HomeStateHolder
import com.yunext.twins.ui.page.home.TwinsHomePage
import com.yunext.twins.ui.page.logger.TwinsLoggerPage
import com.yunext.twins.ui.page.setting.TwinsSettingPage
import com.yunext.twins.ui.theme.CTwinsTheme
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun AppSample() {
    MaterialTheme {
        var greetingText by remember { mutableStateOf("Hello, World!") }
        var showImage by remember { mutableStateOf(false) }
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                greetingText = "Hello, ${getPlatformName()} ${ItemDefaults.randomTextInternal(4)}"
                showImage = !showImage
            }) {
                Text(greetingText)
            }
            AnimatedVisibility(showImage) {
                Image(
                    painterResource("compose-multiplatform.xml"),
                    null
                )
            }
        }
    }

}

/**
 * 适配android沉浸式
 */
val LocalPaddingValues = compositionLocalOf { PaddingValues() }

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App(paddingValues: PaddingValues = PaddingValues()) {
    CompositionLocalProvider(LocalPaddingValues provides paddingValues) {
        CTwinsTheme {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = androidx.compose.material3.MaterialTheme.colorScheme.background
            ) {
                val curScreen: AppDestination by PageStateHolder.state.collectAsState()
//            val list by rememberDeviceAndStateList()
                val list by HomeStateHolder.state.collectAsState(listOf())
                val effect: Effect by HomeStateHolder.effect.collectAsState(Effect.Idle)
                val kvList: List<Int> by DeviceStateHolder.deviceDetailFlow.collectAsState()
                val curDevice: DeviceAndState by remember {
                    mutableStateOf(DeviceAndState.DEBUG_ITEM)
                }
                when (curScreen) {
                    AddDeviceDestination -> TwinsAddDevicePage(
                        onLeft = {
                            PageStateHolder.back()
                        },
                        onDeviceCommit = { deviceName: String, deviceType: DeviceType, deviceCommunicationId: String, deviceModel: String ->
                            HomeStateHolder.addDevice(
                                DeviceAndState(
                                    deviceName,
                                    communicationId = deviceCommunicationId + deviceType,
                                    model = deviceModel,
                                    status = DeviceStatus.random()
                                )
                            )
                            PageStateHolder.back()
                        })

                    BleDestination -> TwinsConfigWifiPage(onLeft = {
                        PageStateHolder.back()
                    })

                    DeviceDestination -> TwinsDevicePage(onLeft = {
                        PageStateHolder.back()
                    }, onRight = {

                    }, onTabSelected = {
                        DeviceStateHolder.detail(it)
                    }, list = kvList, device = curDevice, onMenuClick = {
                        when (it) {
                            MenuData.ConfigWiFi -> {
                                PageStateHolder.skip(BleDestination)
                            }

                            MenuData.Setting -> {
                                PageStateHolder.skip(SettingDestination)
                            }

                            MenuData.Logger -> {
                                PageStateHolder.skip(LogDestination)
                            }

                            MenuData.UpdateTsl -> {

                            }
                        }
                    })

                    Empty, HomeDestination -> {
                        TwinsHomePage(
                            modifier = Modifier,
                            list = list,
                            effect = effect,
                            onDeviceSelected = { device ->
                                DeviceStateHolder.prepareDeviceDetail(device)
                                PageStateHolder.skip(DeviceDestination)
                            },
                            onRefresh = {
                                HomeStateHolder.listDevice()
                            },
                            onActionAdd = {
                                PageStateHolder.skip(AddDeviceDestination)
                            })
                    }

                    LogDestination -> {
                        TwinsLoggerPage(onLeft = {
                            PageStateHolder.skip(DeviceDestination)
                        })
                    }

                    SettingDestination -> {
                        TwinsSettingPage(onLeft = {
                            PageStateHolder.skip(DeviceDestination)
                        })
                    }
                }

                LaunchedEffect(Unit) {
                    HomeStateHolder.listDevice()
                }


            }
        }
    }
}

expect fun getPlatformName(): String




