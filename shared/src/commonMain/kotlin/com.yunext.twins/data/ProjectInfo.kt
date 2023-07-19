package com.yunext.twins.data

data class ProjectInfo(
    val name: String,
    val host: String, val secret: String,
    val brand: String = "skeleton",
)


fun ProjectInfo.isYinDu(): Boolean {
    return secret == "5cdbb8f042004723899f0df7878e7496"
            || brand == "yd"
}