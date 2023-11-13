package com.yunext.twins.module.mqtt.core

import android.annotation.SuppressLint
import java.security.cert.X509Certificate
import javax.net.SocketFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

object DefaultSSL {

    fun defaultFactory(): SocketFactory {
        val manager = @SuppressLint("CustomX509TrustManager")
        object : X509TrustManager {
            override fun checkClientTrusted(
                chain: Array<out X509Certificate>?,
                authType: String?
            ) {
            }

            override fun checkServerTrusted(
                chain: Array<out X509Certificate>?,
                authType: String?
            ) {
            }

            override fun getAcceptedIssuers(): Array<X509Certificate> {
                return arrayOf()
            }
        }

        val sc = SSLContext.getInstance("SSL").apply {
            init(null, arrayOf<TrustManager>(manager), null)
        }
        return sc.socketFactory
    }

}