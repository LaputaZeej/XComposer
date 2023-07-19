package com.yunext.twins.module.http

import com.yunext.twins.module.http.core.HttpResponse

data class LoginResp(
    val token: String? = "",
    val authCode: String? = "",
) : HttpResponse