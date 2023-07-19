package com.yunext.twins.util

import java.security.MessageDigest
import java.util.Locale
import java.util.UUID

actual fun randomText(count: Int): String {
    return UUID.randomUUID().toString().take(count)
}

actual fun md5(text: String, upperCase: Boolean): String? {
    return MD5Util.md5(text, upperCase)
}


private object MD5Util {

    private val UPPER_HEX_DIGITS = charArrayOf(
        '0', '1', '2', '3', '4',
        '5', '6', '7', '8', '9',
        'A', 'B', 'C', 'D', 'E', 'F'
    )

    private val LOWER_HEX_DIGITS = charArrayOf(
        '0', '1', '2', '3', '4',
        '5', '6', '7', '8', '9',
        'a', 'b', 'c', 'd', 'e', 'f'
    )

    private const val MD5 = "MD5"

    fun md5(text: String, upper: Boolean = false): String? {
        val md5 = try {
            MessageDigest.getInstance(MD5)
        } catch (e: Throwable) {
            return null
        }
        val byteArray = text.toByteArray()
        val digest = md5.digest(byteArray)
        val chars = CharArray(digest.size * 2)
        val hexDigits = if (upper) UPPER_HEX_DIGITS else LOWER_HEX_DIGITS
        digest.forEachIndexed { index, byte ->
            chars[index * 2] = hexDigits[byte.toInt() ushr 4 and 0x0f]
            chars[index * 2 + 1] = hexDigits[byte.toInt() and 0x0f]
        }
        return String(chars)
    }

    @Deprecated(
        replaceWith = ReplaceWith("com.bugull.iot.ble.com.bugu.farm.util.MD5Util.md5(text,upper)"),
        message = "md5"
    )
    fun md52(text: String, upper: Boolean = false): String? {
        val md5 = try {
            MessageDigest.getInstance("MD5")
        } catch (e: Throwable) {
            return null
        }
        val byteArray = text.toByteArray()
        val digest = md5.digest(byteArray)
        val stringBuffer = StringBuffer()
        digest.forEach { byte ->
            stringBuffer.append((byte.toInt() ushr 4 and 0x0f).toString(16))
            stringBuffer.append((byte.toInt() and 0x0f).toString(16))
//            val s = (byte.toInt() and 0x0000ff).toString(16)
//            stringBuffer.append(if (s.length == 1) "0$s" else s)
        }
        return stringBuffer.toString().run {
            if (upper) {
                this.uppercase(Locale.getDefault())
            } else this
        }
    }
}
