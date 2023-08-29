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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yunext.twins.base.UiState
import com.yunext.twins.data.DeviceAndState
import com.yunext.twins.data.DeviceStatus
import com.yunext.twins.data.DeviceType
import com.yunext.twins.data.ItemDefaults
import com.yunext.twins.module.repository.RootComponent
import com.yunext.twins.ui.AddDeviceDestination
import com.yunext.twins.ui.AppDestination
import com.yunext.twins.ui.BleDestination
import com.yunext.twins.ui.DeviceDestination
import com.yunext.twins.ui.Empty
import com.yunext.twins.ui.HomeDestination
import com.yunext.twins.ui.LogDestination
import com.yunext.twins.ui.SettingDestination
import com.yunext.twins.ui.page.adddevice.TwinsAddDevicePage
import com.yunext.twins.ui.page.configwifi.TwinsConfigWifiPage
import com.yunext.twins.ui.page.device.DeviceController
import com.yunext.twins.ui.page.device.MenuData
import com.yunext.twins.ui.page.device.TwinsDevicePage
import com.yunext.twins.ui.page.home.AppController
import com.yunext.twins.ui.page.home.TwinsHomePage
import com.yunext.twins.ui.page.logger.TwinsLoggerPage
import com.yunext.twins.ui.page.setting.TwinsSettingPage
import com.yunext.twins.ui.theme.CTwinsTheme
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import kotlin.random.Random

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
    CompositionLocalProvider(  LocalPaddingValues provides paddingValues){
        CTwinsTheme {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = androidx.compose.material3.MaterialTheme.colorScheme.background
            ) {
                var curScreen: AppDestination by remember { mutableStateOf(HomeDestination) }
//            val list by rememberDeviceAndStateList()
                val list by AppController.deviceAndStateListFlow.collectAsState()
                val uiState: UiState<Unit, List<DeviceAndState>> by AppController.uiState.collectAsState()
                val kvList: List<Int> by DeviceController.deviceDetailFlow.collectAsState()
                val curDevice: DeviceAndState by remember {
                    mutableStateOf(DeviceAndState.DEBUG_ITEM)
                }
                val loading by derivedStateOf {
                    when (val tmp = uiState) {
                        is UiState.Fail -> {
                            tmp.error
                            false
                        }

                        is UiState.Processing -> {
                            val progress = tmp.progress
                            val max = tmp.max
                            true
                        }

                        is UiState.Start -> false
                        is UiState.Success -> {
                            tmp.resp
                            false
                        }
                    }
                }
                when (curScreen) {
                    AddDeviceDestination -> TwinsAddDevicePage(
                        onLeft = {
                            curScreen = HomeDestination
                        },
                        onDeviceCommit = { deviceName: String, deviceType: DeviceType, deviceCommunicationId: String, deviceModel: String ->
                            AppController.addDevice(
                                DeviceAndState(
                                    deviceName,
                                    communicationId = deviceCommunicationId + deviceType,
                                    model = deviceModel,
                                    status = DeviceStatus.random()
                                )
                            )
                            curScreen = HomeDestination

                        })

                    BleDestination -> TwinsConfigWifiPage(onLeft = {
                        curScreen = DeviceDestination
                    })

                    DeviceDestination -> TwinsDevicePage(onLeft = {
                        curScreen = HomeDestination
                    }, onRight = {

                    }, onTabSelected = {
                        DeviceController.detail(it)
                    }, list = kvList, device = curDevice, onMenuClick = {
                        when (it) {
                            MenuData.ConfigWiFi -> curScreen = BleDestination

                            MenuData.Setting -> curScreen = SettingDestination

                            MenuData.Logger -> curScreen = LogDestination

                            MenuData.UpdateTsl -> {

                            }
                        }
                    })

                    Empty, HomeDestination -> {
                        TwinsHomePage(
                            modifier = Modifier,
                            list = list,
                            uiState = uiState,
                            onDeviceSelected = { device ->
                                DeviceController.prepareDeviceDetail(device)
                                curScreen = DeviceDestination
                            },
                            onRefresh = {
                                AppController.loadDevice()
                            },
                            onActionAdd = {
                                curScreen = AddDeviceDestination
                            })
                    }

                    LogDestination -> {
                        TwinsLoggerPage(onLeft = { curScreen = DeviceDestination })
                    }

                    SettingDestination -> {
                        TwinsSettingPage(onLeft = {
                            curScreen = DeviceDestination
                        })
                    }
                }

                LaunchedEffect(Unit) {
                    AppController.loadDevice()
                }
            }
        }
    }
}

expect fun getPlatformName(): String

