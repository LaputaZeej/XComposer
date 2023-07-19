package com.yunext.twins.data.mqtt

sealed class ProtocolMQTTTopic(
    val category: String,
    private val pubSupports: Array<ProtocolMQTTRule>,
    private val subSupports: Array<ProtocolMQTTRule>,
    private val desc: String
) {
    object UP : ProtocolMQTTTopic(
        "up",
        arrayOf(ProtocolMQTTRule.Device),
        arrayOf(
            ProtocolMQTTRule.Web,
            ProtocolMQTTRule.App.Mini,
            ProtocolMQTTRule.App.H5,
            ProtocolMQTTRule.App.Ios,
            ProtocolMQTTRule.App.Android
        ),
        "设备上报属性状态、故障消息"
    )

    object DOWN : ProtocolMQTTTopic(
        "down",
        arrayOf(
            ProtocolMQTTRule.Web,
            ProtocolMQTTRule.App.Mini,
            ProtocolMQTTRule.App.H5,
            ProtocolMQTTRule.App.Ios,
            ProtocolMQTTRule.App.Android
        ),
        arrayOf(ProtocolMQTTRule.Device),
        "下发控制指令至设备"
    )

    object QUERY : ProtocolMQTTTopic(
        "query",
        arrayOf(ProtocolMQTTRule.Device),
        arrayOf(ProtocolMQTTRule.Web),
        "设备主动查询信息"
    )

    object REPLY : ProtocolMQTTTopic(
        "reply",
        arrayOf(ProtocolMQTTRule.Web),
        arrayOf(ProtocolMQTTRule.Device),
        "回复消息给设备"
    )

    object STATUS : ProtocolMQTTTopic(
        "status",
        arrayOf(ProtocolMQTTRule.Web),
        arrayOf(ProtocolMQTTRule.App.Mini, ProtocolMQTTRule.App.H5, ProtocolMQTTRule.App.Ios, ProtocolMQTTRule.App.Android),
        "服务端发布设备状态消息给APP、小程序"
    )


//    object ALERT : MQTTTopic(
//        "alert",
//        arrayOf(MQTTRule.Device),
//        arrayOf(
//            MQTTRule.Web,
//            MQTTRule.App.Mini,
//            MQTTRule.App.H5,
//            MQTTRule.App.Ios,
//            MQTTRule.App.Android
//        ),
//        "设备上报属性状态、故障消息"
//    )
//
//    object INFO : MQTTTopic(
//        "info",
//        arrayOf(MQTTRule.Device),
//        arrayOf(
//            MQTTRule.Web,
//            MQTTRule.App.Mini,
//            MQTTRule.App.H5,
//            MQTTRule.App.Ios,
//            MQTTRule.App.Android
//        ),
//        "设备上报属性状态、故障消息"
//    )
//
//    object ERROR : MQTTTopic(
//        "alert",
//        arrayOf(MQTTRule.Device),
//        arrayOf(
//            MQTTRule.Web,
//            MQTTRule.App.Mini,
//            MQTTRule.App.H5,
//            MQTTRule.App.Ios,
//            MQTTRule.App.Android
//        ),
//        "设备上报属性状态、故障消息"
//    )

}


