package com.yunext.twins.data.tsl

enum class TslEventType(val text: String) {
    ALERT("alert"),
    INFO("info"),
    ERROR("error");
    companion object{
        fun from(text: String): TslEventType {
            return when(text){
                ALERT.text-> ALERT
                INFO.text-> INFO
                ERROR.text-> ERROR
                else-> throw TslException("不支持的TslEventType:$text")
            }
        }
    }
}