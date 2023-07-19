package com.yunext.twins.data.mqtt

sealed class ProtocolMQTTRule {
    object Device : ProtocolMQTTRule()
    object Web : ProtocolMQTTRule()
    sealed class App : ProtocolMQTTRule() {
        object Mini : App()
        object H5 : App()
        object Ios : App()
        object Android : App()
    }
}