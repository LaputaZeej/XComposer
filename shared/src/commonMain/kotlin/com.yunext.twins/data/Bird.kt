package com.yunext.twins.data

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Bird.toJson() = Json.encodeToString(this)

@Serializable
data class Bird(val name: String, val color: Int) {
    companion object {
        // https://kotlinlang.org/docs/serialization.html#example-json-serialization
        // https://github.com/Kotlin/kotlinx.serialization/blob/master/docs/serialization-guide.md
        fun fromJson(json: String) = Json.decodeFromString<Bird>(json)

        fun test() {
            val bird = Bird("a", 1)
            val json = Json.encodeToString(bird)
            println("json  = $json")
            println(
                "json  = ${
                    fromJson(
                        """
                {
                    "name":"b",
                    "color":2
                }
            """.trimIndent()
                    )
                }"
            )
        }
    }
}