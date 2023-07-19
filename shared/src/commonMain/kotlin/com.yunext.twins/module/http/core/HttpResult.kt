package com.yunext.twins.module.http.core

sealed class HttpResult<out T> {
    data class Success<T>(val data: T) : HttpResult<T>()
    data class Fail(val error: Throwable) : HttpResult<Nothing>()
}

fun <T> httpSuccess(data: T): HttpResult<T> = HttpResult.Success(data)
fun <T> httpFail(e: Throwable, code: Int = -1): HttpResult<T> =
    HttpResult.Fail(ApiException(code, e.message ?: "", e))

fun <T, R> HttpResult<T>.map(map: (T) -> R): HttpResult<R> {
    return when (this) {
        is HttpResult.Fail -> this
        is HttpResult.Success -> httpSuccess(map(this.data))
    }
}