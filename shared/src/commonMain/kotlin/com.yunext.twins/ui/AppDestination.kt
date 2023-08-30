package com.yunext.twins.ui

sealed interface AppDestination {
    val route: String
    //val screen: @Composable () -> Unit  // 实际中有很多参数要传递 所以移动到XNavHost中
}

fun AppDestination.isEmpty() = this == Empty
fun AppDestination.isRoot() = this == Empty || this == HomeDestination
fun AppDestination.back():AppDestination{
    return when(this){
        AddDeviceDestination -> HomeDestination
        BleDestination -> DeviceDestination
        DeviceDestination -> HomeDestination
        Empty -> Empty
        HomeDestination -> Empty
        LogDestination -> DeviceDestination
        SettingDestination -> DeviceDestination
    }
}

val APP_PAGES = listOf(
    Empty,
    HomeDestination,
    AddDeviceDestination,
    DeviceDestination,
    BleDestination,
    LogDestination,
    SettingDestination
)

object Empty : AppDestination {
    override val route: String
        get() = "empty"

}

object HomeDestination : AppDestination {
    override val route: String
        get() = "home"

}

object AddDeviceDestination : AppDestination {
    override val route: String
        get() = "add_device"

}

object DeviceDestination : AppDestination {
    override val route: String
        get() = "device"


}

object BleDestination : AppDestination {
    override val route: String
        get() = "ble"

}

object LogDestination : AppDestination {
    override val route: String
        get() = "log"

}

object SettingDestination : AppDestination {
    override val route: String
        get() = "setting"
}