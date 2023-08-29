package com.yunext.twins.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.random.Random

@Serializable
data class NewsVo(
    val id: Int,
    val title: String,
    val summary: String,
    val date: String,
    val imageUrl: String,
) {
    companion object {
        internal fun random() = if (Random.nextBoolean())
            randomNews() else fromJson()

        internal fun fromJson(json: String = DEMO_JSON): NewsVo {
            return Json.decodeFromString(json)
        }

        internal fun fromJsonList(json: String = DEMO_JSON_LIST): List<NewsVo> {
            return Json.decodeFromString(json)
        }
    }
}

private fun randomNews() = NewsVo(
    id = Random.nextInt(1000),
    title = "title-${Random.nextInt(100)}",
    summary = "summary-${Random.nextInt(100)}",
    date = "date-${Random.nextInt(100)}",
    imageUrl = "imageUrl-${Random.nextInt(100)}"
)

fun NewsVo.toJson(): String {
    return Json.encodeToString(this)
}

private const val DEMO_JSON = """
    {
        "id":123456,
        "title":"title-1234",
        "summary":"summary-1234",
        "date":"date-1234",
        "imageUrl":"imageUrl-1234"
    }
"""

private const val DEMO_JSON_LIST = """
   [
    {
        "id":123456,
        "title":"title-1234",
        "summary":"summary-1234",
        "date":"date-1234",
        "imageUrl":"imageUrl-1234"
    },
     {
        "id":1234567,
        "title":"title-12347",
        "summary":"summary-12347",
        "date":"date-12347",
        "imageUrl":"imageUrl-12347"
    },
     {
        "id":1234568,
        "title":"title-12348",
        "summary":"summary-12348",
        "date":"date-12348",
        "imageUrl":"imageUrl-12348"
    }
   ]
"""