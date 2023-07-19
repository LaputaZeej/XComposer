package com.yunext.twins.data.tsl

open class TslException(
    message: String?,
    cause: Throwable? = null,
    ) : Throwable(message, cause) {
}
class TslCmdException(message: String?) : TslException(message,null)