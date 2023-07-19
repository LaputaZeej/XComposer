package com.yunext.twins.data.tsl.property

import com.yunext.twins.base.TODO
import com.yunext.twins.base.logger.DefaultLogger
import com.yunext.twins.data.tsl.Tsl
import com.yunext.twins.data.tsl.TslException
import com.yunext.twins.data.tsl.TslItemProperty
import com.yunext.twins.data.tsl.TslProperty
import com.yunext.twins.data.tsl.TslPropertyType


/**
 * ===========================更新Map[1]================================
 * 从Tsl初始化属性值
 * 初始化的数据 用于后面的数据更新,见[tslHandleUpdatePropertyValueFromJson]
 * Tsl: 物模型语言
 *
 * @return 物的属性值map key为物的属性id 值为PropertyValue
 * ============================================================
 */
fun Tsl.tslHandleTsl2PropertyValues(): Map<String, PropertyValue<*>> {
    logger.ld("tslHandleTsl2PropertyValues")
    val parseTsl2PropertyKeys = this.tslHandleParsePropertyKeys()
    return parseTsl2PropertyKeys.map {
        val sIdentifier = it.key
        val sPropertyKey = it.value
        val result: Pair<String, PropertyValue<*>> =
            sIdentifier to sPropertyKey.tslHandleDefaultValue()
        result
    }.toMap().also {
        logger.ld("tslHandleTsl2PropertyValues end. size= ${it.size}")
    }
}

/**
 * StructArrayPropertyKey 创建默认的struct对象
 */
fun StructArrayPropertyKey.defaultItemValue(): StructPropertyValue {
    val key: StructPropertyKey = StructPropertyKey.fake(this.itemKeys)
    val v = this.itemKeys.map {
        it to it.tslHandleDefaultValue()
    }.toMap()
    return StructPropertyValue(key, v)
}

/*
 * 每个PropertyKey 初始化后的默认值
 * PropertyKey -> PropertyValue
 */
fun PropertyKey.tslHandleDefaultValue(): PropertyValue<*> {
    return when (this) {
        is ArrayPropertyKey -> {
            val arrayPropertyKey: ArrayPropertyKey = this
            when (arrayPropertyKey) {
                is IntArrayPropertyKey -> IntArrayPropertyValue(arrayPropertyKey, listOf())
                is DoubleArrayPropertyKey -> DoubleArrayPropertyValue(
                    arrayPropertyKey,
                    listOf()
                )
                is FloatArrayPropertyKey -> FloatArrayPropertyValue(arrayPropertyKey, listOf())
                is TextArrayPropertyKey -> TextArrayPropertyValue(arrayPropertyKey, listOf())
                is StructArrayPropertyKey -> StructArrayPropertyValue(arrayPropertyKey, listOf())
            }
        }
        is BooleanPropertyKey -> BoolPropertyValue(this, null)
        is DatePropertyKey -> DatePropertyValue(this, null)
        is DoublePropertyKey -> DoublePropertyValue(this, null)
        is IntEnumPropertyKey -> IntEnumPropertyValue.from(this)
        is TextEnumPropertyKey -> TextEnumPropertyValue.from(this)
        is FloatPropertyKey -> FloatPropertyValue(this, null)
        is IntPropertyKey -> IntPropertyValue(this, null)
        is StructPropertyKey -> {
            val items = this.items
            val map = items.map { propertyKey ->
                propertyKey to propertyKey.tslHandleDefaultValue()
            }.toMap()
            StructPropertyValue(this, map)
        }
        is TextPropertyKey -> TextPropertyValue(this, null)
    }
}


/**
 * =============================更新Map[2]===============================
 * 从json数据更新数据
 *
 * @param map 原来的数据
 * @param json json
 * @return 更新过后的数据 + 更新的数据
 * ============================================================
 */
fun tslHandleUpdatePropertyValuesFromJson(
    map: Map<String, PropertyValue<*>>,
    json: String? = null
): Pair<Map<String, PropertyValue<*>>, Map<String, PropertyValue<*>>> {
    json ?: return map to mapOf()
    if (json.isBlank()) return map to mapOf()
    fun i(msg: Any?, lows: Boolean = false) {
        val prefix = if (lows) "----" else ""
        logger.li(prefix + msg)
    }
    // 原始的数据
    val oldMap = map.toMutableMap()
    val temp: MutableMap<String, PropertyValue<*>> = mutableMapOf()
    i("###############################################START")
    i("tslHandleUpdatePropertyValuesFromJson start...")
    i("json = $json")
    i("size = " + oldMap.size)
    // 从json入手 有什么更新什么。【前提条件】 第一次初始化value都全部包含了，也必须按照TSL严格初始化，设置默认值。
    val iterator = map.iterator()
    while (iterator.hasNext()) {
        val next = iterator.next()
        val identifier = next.key
        val propertyKey = next.value.key
        val value = next.value
        i("【$identifier】${propertyKey.type} -${next.value.key}")
        val fromJson = value.tslHandleUpdatePropertyValueFromJson(json)
        if (fromJson != null) {
            oldMap[identifier] = fromJson
            temp[identifier] = fromJson
        }

    }
    i("###############################################END")
    return oldMap to temp
}

/**
 * =============================更新Map[3]===============================
 * 从list中更新map
 * @param map 原始的map
 * @param list 新的值 需要更新的值
 * @return 得到新的值map
 * ===========================================================
 */
fun tslHandleUpdatePropertyValues(
    map: Map<String, PropertyValue<*>>,
    list: List<PropertyValue<*>>
): Map<String, PropertyValue<*>> {
    if (list.isEmpty()) return map
    fun i(msg: Any?, lows: Boolean = false) {
        val prefix = if (lows) "----" else ""
        logger.li(prefix + msg)
    }
    // 原始的数据
    val oldMap = map.toMutableMap()
    i("###############################################START")
    i("tslHandleUpdatePropertyValues start...")
    i("list = $list")
    i("size = " + oldMap.size)
    val iterator = map.iterator()
    while (iterator.hasNext()) {
        val next = iterator.next()
        val identifier = next.key
        list.forEach { listPV ->
            if (listPV.key.identifier == identifier) {
                oldMap[identifier] = listPV
            }
        }
    }
    i("###############################################END")
    return oldMap
}

/**
 * ============================================================
 * 把物的属性值转成键值对，以便生成json数据
 *
 * @param structInArray 当是array<struct>时候，默认值
 * @return 键值对 key为物的属性id，值为any
 * ============================================================
 */
fun PropertyValue<*>.tslHandleToJsonValue(structInArray: Boolean = false): Pair<String, Any?> {
    val identifier = this.key.identifier
    when (this) {
        is DoubleArrayPropertyValue -> {
            val propertyKey = this.key
            val size = propertyKey.size
            return identifier to this.value.take(size)
        }
        is FloatArrayPropertyValue -> {
            val propertyKey = this.key
            val size = propertyKey.size
            return identifier to this.value.take(size)
        }
        is IntArrayPropertyValue -> {
            val propertyKey = this.key
            val size = propertyKey.size
            return identifier to this.value.take(size)
        }
        is TextArrayPropertyValue -> {
            val propertyKey = this.key
            val size = propertyKey.size
            return identifier to this.value.take(size)
        }
        is BoolPropertyValue -> {
            return identifier to this.value.run {
                this ?: if (structInArray) false else null
            }
        }
        is DatePropertyValue -> {
            return identifier to this.value.run {
                this ?: if (structInArray) "" else null
            }
        }
        is DoublePropertyValue -> {
            return identifier to this.value.run {
                this ?: if (structInArray) 0.0 else null
            }
        }
        is FloatPropertyValue -> {
            return identifier to this.value.run {
                this ?: if (structInArray) 0f else null
            }
        }
        is IntEnumPropertyValue -> {
            val v = if (this.isEmpty()) {
                if (structInArray) 0 else null
            } else {
                this.keyValue.value
            }
            return identifier to v
        }
        is IntPropertyValue -> {
            return identifier to this.value.run {
                this ?: if (structInArray) false else null
            }
        }
        is StructPropertyValue -> {
            val itemValues = this.itemValues
            val map: MutableMap<String, Any> = mutableMapOf()
            itemValues.forEach {
                val k = it.key.identifier
                val v = it.value
                val gsonValue = v.tslHandleToJsonValue().second
                if (gsonValue != null) {
                    map[k] = gsonValue
                }
            }
            return identifier to map
        }
        is TextEnumPropertyValue -> {
            val v = if (this.isEmpty()) {
                if (structInArray) "" else null
            } else {
                this.keyValue.value
            }
            return identifier to v
        }
        is TextPropertyValue -> {
            return identifier to this.value.run {
                this ?: if (structInArray) "" else null
            }
        }
        is StructArrayPropertyValue -> {
            // :{"twins_array":[{"first":"fake_StructArrayPropertyKey_identifier","second":{"twins_array_a":0.0555,"twins_array_b":true}},{"first":"fake_StructArrayPropertyKey_identifier","second":{"twins_array_a":555866.0}}]}}
            val propertyKey = this.key
            val size = propertyKey.size
            val sourceList = this.value
            val list: MutableList<Any> = mutableListOf()
            DefaultLogger.ld("v = ${this.displayValue}")
            // 1.先遍历StructArrayPropertyValue::value
            sourceList.take(size).forEach { structPropertyValue: StructPropertyValue ->
                // 2.再遍历StructPropertyValue::itemValues
                val innerMap: MutableMap<String, Any?> = mutableMapOf()
                structPropertyValue.itemValues.forEach { innerItem ->
                    val innerKey = innerItem.key
                    val innerValue = innerItem.value
                    val finalValue = innerValue.tslHandleToJsonValue(true)
                    // 3.取出属性键值对，组成struct，即innerMap
                    innerMap[innerKey.identifier] = finalValue.second
                }
                list.add(innerMap)
            }
            DefaultLogger.ld(list.joinToString { it.toString() })
            return identifier to list
        }
    }
}

fun List<PropertyValue<*>>.tslHandleToJsonValues(): Map<String, Any> {
    val map: MutableMap<String, Any> = mutableMapOf()
    this.map { propertyValue ->
        val r = propertyValue.tslHandleToJsonValue()
        val v = r.second
        if (v != null) {
            map[r.first] = v
        }
    }
    return map
}

// TODO("序列化")
//fun List<PropertyValue<*>>.tslHandleToJsonObject(): String {
//    val jsonObject = JSONObject()
//    this.forEach { propertyValue ->
//        val tslHandleToJsonValue = propertyValue.tslHandleToJsonValue()
//        val v = tslHandleToJsonValue.second
//        if (v != null) {
//            jsonObject.put(propertyValue.key.identifier, v)
//        }
//    }
//    return jsonObject.toString()
//}

/**
 * 解析TslProperty转化成分类的PropertyKey
 *
 * @param tslProperty 未处理未分类过的原始的属性
 * @return 分类好的PropertyKey
 */
internal fun tslHandleParsePropertyKey(
    tslProperty: TslProperty
): PropertyKey? {
    val identifier = tslProperty.identifier
    val inner = tslProperty.inner
    val prefix = if (inner) "|————" else "|"
    return when (tslProperty.dataType) {
        TslPropertyType.INT -> {
            logger.ld("$prefix$identifier int")
            IntPropertyKey(
                false,
                tslProperty.accessMode,
                tslProperty.required,
                tslProperty.desc,
                tslProperty.identifier,
                tslProperty.name,
                max = tslProperty.specs?.max?.toString()?.toDouble()?.toInt() ?: 0,
                min = tslProperty.specs?.min?.toString()?.toDouble()?.toInt() ?: 0,
                unit = tslProperty.specs?.unit?.toString() ?: "",
                step = tslProperty.specs?.step?.toString() ?: ""
            )
        }

        TslPropertyType.FLOAT -> {
            logger.ld("$prefix$identifier float")
            FloatPropertyKey(
                false,
                tslProperty.accessMode,
                tslProperty.required,
                tslProperty.desc,
                tslProperty.identifier,
                tslProperty.name,
                max = tslProperty.specs?.max?.toString()?.toDouble()?.toFloat() ?: 0f,
                min = tslProperty.specs?.min?.toString()?.toDouble()?.toFloat() ?: 0f,
                unit = tslProperty.specs?.unit?.toString() ?: "",
                tslProperty.specs?.step?.toString() ?: ""
            )
        }

        TslPropertyType.DOUBLE -> {
            logger.ld("$prefix$identifier double")
            DoublePropertyKey(
                false,
                tslProperty.accessMode,
                tslProperty.required,
                tslProperty.desc,
                tslProperty.identifier,
                tslProperty.name,
                max = tslProperty.specs?.max?.toString()?.toDouble() ?: 0.0,
                min = tslProperty.specs?.min?.toString()?.toDouble() ?: 0.0,
                unit = tslProperty.specs?.unit?.toString() ?: "",
                tslProperty.specs?.step?.toString() ?: ""
            )
        }

        TslPropertyType.TEXT -> {
            logger.ld("$prefix$identifier text")
            TextPropertyKey(
                false,
                tslProperty.accessMode,
                tslProperty.required,
                tslProperty.desc,
                tslProperty.identifier,
                tslProperty.name,
                length = tslProperty.specs?.length?.toString()?.toInt() ?: 0
            )

        }

        TslPropertyType.DATE -> {
            logger.ld("$prefix$identifier date")
            DatePropertyKey(
                false,
                tslProperty.accessMode,
                tslProperty.required,
                tslProperty.desc,
                tslProperty.identifier,
                tslProperty.name,
            )
        }

        TslPropertyType.BOOL -> {
            logger.ld("$prefix$identifier bool")
            BooleanPropertyKey(
                false,
                tslProperty.accessMode,
                tslProperty.required,
                tslProperty.desc,
                tslProperty.identifier,
                tslProperty.name,

                specs = tslProperty.specs?.enumDesc?.let {
                    try {
                        val json = it.toString()
                        parseEnumText(json)
                    } catch (e: Throwable) {
                        e.printStackTrace()
                        null
                    }
                }
                    ?: throw TslException("bool类型值异常:${tslProperty.specs?.enumDesc} @${identifier}")
            )
        }

        TslPropertyType.ENUM -> {
            logger.ld("$prefix$identifier enum")
            val type = tslProperty.specs?.type?.toString() ?: ""
            when (TslPropertyType.from(type)) {
                TslPropertyType.INT -> IntEnumPropertyKey(
                    false,
                    tslProperty.accessMode,
                    tslProperty.required,
                    tslProperty.desc,
                    tslProperty.identifier,
                    tslProperty.name,
                    specs = tslProperty.specs?.enumDesc?.let {
                        try {
                            val json = it.toString()
                            parseEnumText(json).map { pair ->
                                IntEnumPropertyValue.KeyValue(pair.first, pair.second)
                            }
                        } catch (e: Throwable) {
                            e.printStackTrace()
                            null
                        }
                    } ?: listOf()
                )
                TslPropertyType.TEXT -> TextEnumPropertyKey(
                    false,
                    tslProperty.accessMode,
                    tslProperty.required,
                    tslProperty.desc,
                    tslProperty.identifier,
                    tslProperty.name,
                    specs = tslProperty.specs?.enumDesc?.let {
                        try {
                            val json = it.toString()
                            parseEnumTextString(json).map { pair ->
                                TextEnumPropertyValue.KeyValue(pair.first, pair.second)
                            }
                        } catch (e: Throwable) {
                            e.printStackTrace()
                            null
                        }
                    } ?: listOf()
                )
                else -> throw TslException("enum不支持的格式:$type @$identifier")
            }

        }

        TslPropertyType.STRUCT -> {
            logger.ld("$prefix$identifier struct inner:${tslProperty.inner}")
            if (tslProperty.inner) return null
            StructPropertyKey(
                tslProperty.accessMode,
                tslProperty.required,
                tslProperty.desc,
                tslProperty.identifier,
                tslProperty.name,
                items = run {
                    val properties = (tslProperty.specs?.item?.let {
                        TslItemProperty.from(it.toString())
                    })
                    if (properties == null || properties.isEmpty()) throw TslException("struct没有指定的item")
                    properties.map {
                        tslHandleParsePropertyKey(TslProperty.from(it))
                    }.filterNotNull()
                }
            )
        }

        TslPropertyType.ARRAY -> {
            logger.ld("$prefix$identifier array inner:${tslProperty.inner}")
            if (tslProperty.inner) return null
            when (val itemType = TslPropertyType.from(tslProperty.specs?.type?.toString() ?: "")) {
                TslPropertyType.INT -> {
                    IntArrayPropertyKey(
                        false,
                        tslProperty.accessMode,
                        tslProperty.required,
                        tslProperty.desc,
                        tslProperty.identifier,
                        tslProperty.name,
                        size = tslProperty.specs?.length ?: 0
                    )
                }
                TslPropertyType.FLOAT -> {
                    FloatArrayPropertyKey(
                        false,
                        tslProperty.accessMode,
                        tslProperty.required,
                        tslProperty.desc,
                        tslProperty.identifier,
                        tslProperty.name,
                        size = tslProperty.specs?.length ?: 0
                    )
                }
                TslPropertyType.DOUBLE -> DoubleArrayPropertyKey(
                    false,
                    tslProperty.accessMode,
                    tslProperty.required,
                    tslProperty.desc,
                    tslProperty.identifier,
                    tslProperty.name,
                    size = tslProperty.specs?.length ?: 0
                )
                TslPropertyType.TEXT -> TextArrayPropertyKey(
                    false,
                    tslProperty.accessMode,
                    tslProperty.required,
                    tslProperty.desc,
                    tslProperty.identifier,
                    tslProperty.name,
                    size = tslProperty.specs?.length ?: 0
                )
                TslPropertyType.DATE -> throw TslException("array不支持类型$itemType @$identifier ")
                TslPropertyType.BOOL -> throw TslException("array不支持类型$itemType @$identifier ")
                TslPropertyType.ENUM -> throw TslException("array不支持类型$itemType @$identifier ")
                TslPropertyType.STRUCT ->
                    StructArrayPropertyKey(
                        false,
                        tslProperty.accessMode,
                        tslProperty.required,
                        tslProperty.desc,
                        tslProperty.identifier,
                        tslProperty.name,
                        size = tslProperty.specs?.length ?: 0,
                        itemKeys = tslProperty.specs?.item?.let {
                            val json = it.toString()
                            val itemProperty = TslItemProperty.from(json)
                            if (itemProperty.isEmpty()) throw TslException("struct没有指定的item")
                            itemProperty.map {
                                tslHandleParsePropertyKey(TslProperty.from(it))
                            }.filterNotNull()
                        } ?: listOf()
                    )

                TslPropertyType.ARRAY -> {
                    throw TslException("array不支持类型$itemType @$identifier ")
                }
            }

        }
    }
}


private fun parseEnumTextString(json: String): List<Pair<String, String>> {
    if (json.isEmpty()) return listOf()
    return try {
        @TODO("序列化")
        listOf()
        //gson.fromJson<Map<String, String>>(json, Map::class.java).toList()
    } catch (e: Throwable) {
        listOf()
    }
}

private fun parseEnumText(json: String): List<Pair<Int, String>> {
    if (json.isEmpty()) return listOf()
    return try {
//        gson.fromJson<Map<String, String>>(json, Map::class.java).map {
//            it.key.toInt() to it.value
//        }
        @TODO("序列化")
        listOf()
    } catch (e: Throwable) {
        listOf()
    }
}

/**
 * 从json更新老的数据
 * 只修改获得的值，未获得的值保持原来的值
 *
 * @param json 收到的json数据
 * @return 新的值
 *
 */
fun PropertyValue<*>.tslHandleUpdatePropertyValueFromJson(json: String?): PropertyValue<*>? {
    logger.ld("    tslHandleUpdatePropertyValueFromJson-${this.key.identifier} json:$json")
    json ?: return null
    if (json.isBlank()) return null
    TODO("序列化")
    return null
//    return when (this) {
//        is DoubleArrayPropertyValue -> {
//            try {
//                val sPropertyKey = this.key
//                val sIdentifier = sPropertyKey.identifier
//                tryGetJSON<JSONObject>(json)?.let { jsonObject ->
//                    val jsonArray = jsonObject.getJSONArray(sIdentifier)
//                    val list: MutableList<Double> = mutableListOf()
//                    val length = jsonArray.length()
//                    if (length > 0) {
//                        (0 until length).forEach {
//                            try {
//                                val r = jsonArray.getDouble(it)
//                                list.add(r)
//                            } catch (e: Throwable) {
//                                e.printStackTrace()
//                            }
//                        }
//                    }
//                    DoubleArrayPropertyValue(
//                        sPropertyKey,
//                        list
//                    )
//                }
//            } catch (e: Throwable) {
//                e.printStackTrace()
//                null
//            }
//        }
//        is FloatArrayPropertyValue -> {
//            try {
//                val sPropertyKey = this.key
//                val sIdentifier = sPropertyKey.identifier
//                tryGetJSON<JSONObject>(json)?.let { jsonObject ->
//                    val jsonArray = jsonObject.getJSONArray(sIdentifier)
//                    val list: MutableList<Float> = mutableListOf()
//                    val length = jsonArray.length()
//                    if (length > 0) {
//                        (0 until length).forEach {
//                            try {
//                                val r = jsonArray.getDouble(it)
//                                list.add(r.toFloat())
//                            } catch (e: Throwable) {
//                                e.printStackTrace()
//                            }
//                        }
//                    }
//                    FloatArrayPropertyValue(
//                        sPropertyKey,
//                        list
//                    )
//                }
//            } catch (e: Throwable) {
//                e.printStackTrace()
//                null
//            }
//        }
//        is IntArrayPropertyValue -> {
//            try {
//                val sPropertyKey = this.key
//                val sIdentifier = sPropertyKey.identifier
//                val fv: IntArrayPropertyValue? = tryGetJSON<JSONObject>(json)?.let { jsonObject ->
//                    val jsonArray = jsonObject.getJSONArray(sIdentifier)
//                    val list: MutableList<Int> = mutableListOf()
//                    val length = jsonArray.length()
//                    if (length > 0) {
//                        (0 until length).forEach {
//                            try {
//                                val r = jsonArray.getInt(it)
//                                list.add(r)
//                            } catch (e: Throwable) {
//                                e.printStackTrace()
//                            }
//                        }
//                    }
//                    IntArrayPropertyValue(
//                        sPropertyKey,
//                        list
//                    )
//                }
//                fv
//            } catch (e: Throwable) {
//                e.printStackTrace()
//                null
//            }
//        }
//        is TextArrayPropertyValue -> {
//            try {
//                val sPropertyKey = this.key
//                val sIdentifier = sPropertyKey.identifier
//                tryGetJSON<JSONObject>(json)?.let { jsonObject ->
//                    val jsonArray = jsonObject.getJSONArray(sIdentifier)
//                    val list: MutableList<String> = mutableListOf()
//                    val length = jsonArray.length()
//                    if (length > 0) {
//                        (0 until length).forEach {
//                            try {
//                                val r = jsonArray.getString(it)
//                                list.add(r)
//                            } catch (e: Throwable) {
//                                e.printStackTrace()
//                            }
//                        }
//                    }
//                    TextArrayPropertyValue(
//                        sPropertyKey,
//                        list
//                    )
//                }
//            } catch (e: Throwable) {
//                e.printStackTrace()
//                null
//            }
//        }
//        is BoolPropertyValue -> {
//            try {
//                val sPropertyKey = this.key
//                val sIdentifier = sPropertyKey.identifier
//                val jsonObject = tryGetJSON<JSONObject>(json)
//                val v = jsonObject?.getBoolean(sIdentifier)
//                if (v == null) {
//                    null
//                } else {
//                    BoolPropertyValue(sPropertyKey, v)
//                }
//            } catch (e: Throwable) {
//                e.printStackTrace()
//                null
//            }
//        }
//        is DatePropertyValue -> {
//            try {
//                val sPropertyKey = this.key
//                val sIdentifier = sPropertyKey.identifier
//                val jsonObject = tryGetJSON<JSONObject>(json)
//                val v = jsonObject?.getString(sIdentifier)
//                if (v == null) {
//                    null
//                } else {
//                    DatePropertyValue(sPropertyKey, v)
//                }
//            } catch (e: Throwable) {
//                e.printStackTrace()
//                null
//            }
//        }
//        is DoublePropertyValue -> {
//            try {
//                val sPropertyKey = this.key
//                val sIdentifier = sPropertyKey.identifier
//                val jsonObject = tryGetJSON<JSONObject>(json)
//                val v = jsonObject?.getDouble(sIdentifier)
//                if (v == null) {
//                    null
//                } else {
//                    DoublePropertyValue(sPropertyKey, v)
//                }
//            } catch (e: Throwable) {
//                e.printStackTrace()
//                null
//            }
//        }
//        is FloatPropertyValue -> {
//            try {
//                val sPropertyKey = this.key
//                val sIdentifier = sPropertyKey.identifier
//                val jsonObject = tryGetJSON<JSONObject>(json)
//                val v = jsonObject?.getDouble(sIdentifier)
//                if (v == null) {
//                    null
//                } else {
//                    FloatPropertyValue(sPropertyKey, v.toFloat())
//                }
//            } catch (e: Throwable) {
//                e.printStackTrace()
//                null
//            }
//        }
//        is TextEnumPropertyValue -> {
//            try {
//                val sPropertyKey = this.key
//                val sIdentifier = sPropertyKey.identifier
//                val jsonObject = tryGetJSON<JSONObject>(json)
//                val v = jsonObject?.getString(sIdentifier)
//                if (v == null) {
//                    null
//                } else {
//                    TextEnumPropertyValue.from(sPropertyKey, v)
//                }
//            } catch (e: Throwable) {
//                e.printStackTrace()
//                null
//            }
//        }
//        is TextPropertyValue -> {
//            try {
//                val sPropertyKey = this.key
//                val sIdentifier = sPropertyKey.identifier
//                val jsonObject = tryGetJSON<JSONObject>(json)
//                val v = jsonObject?.getString(sIdentifier)
//                if (v == null) {
//                    null
//                } else {
//                    TextPropertyValue(sPropertyKey, v)
//                }
//            } catch (e: Throwable) {
//                e.printStackTrace()
//                null
//            }
//        }
//        is IntEnumPropertyValue -> {
//            try {
//                val sPropertyKey = this.key
//                val sIdentifier = sPropertyKey.identifier
//                val jsonObject = tryGetJSON<JSONObject>(json)
//                val v = jsonObject?.getInt(sIdentifier)
//                if (v == null) {
//                    null
//                } else {
//                    IntEnumPropertyValue.from(sPropertyKey, v)
//                }
//            } catch (e: Throwable) {
//                e.printStackTrace()
//                null
//            }
//        }
//        is IntPropertyValue -> {
//            try {
//                val sPropertyKey = this.key
//                val sIdentifier = sPropertyKey.identifier
//                val jsonObject = tryGetJSON<JSONObject>(json)
//                val v = jsonObject?.getInt(sIdentifier)
//                if (v == null) {
//                    null
//                } else {
//                    IntPropertyValue(sPropertyKey, v)
//                }
//            } catch (e: Throwable) {
//                e.printStackTrace()
//                null
//            }
//        }
//        is StructPropertyValue -> {
//            // {"twins_struct":{"twins_struct_a":55,"twins_struct_b":"hhhhh","twins_struct_c":0.58866,"twins_struct_d":"bbbbb","twins_struct_e":true,"twins_struct_f":1,"twins_struct_g":"dong"}}}
//            try {
//                val sPropertyKey = this.key
//                val sIdentifier = sPropertyKey.identifier
//                val fake = sPropertyKey.fake
//                DefaultLogger.ld("$sIdentifier : fake = $fake")
//                if (fake) {
//                    fun findIdentifierWithBlock(block: (String) -> Boolean) {
//                        val itemJsonObject = JSONObject(json)
//                        block.invoke(itemJsonObject.toString())
//                    }
//
//                    val itemValues = this.itemValues
//                    DefaultLogger.ld("itemValues = ${itemValues.size}")
//                    val sMap: MutableMap<PropertyKey, PropertyValue<*>> = itemValues.toMutableMap()
//                    itemValues.forEach { sItem ->
//                        val sPkIdentifier = sItem.key
//                        val sValue = sItem.value
//                        findIdentifierWithBlock {
//                            val fromJson = sValue.tslHandleUpdatePropertyValueFromJson(it)
//                            if (fromJson != null) {
//                                sMap[sPkIdentifier] = fromJson
//                                true
//                            } else {
//                                false
//                            }
//                        }
//                    }
//                    StructPropertyValue(sPropertyKey, sMap)
//                } else {
//                    fun findIdentifierWithBlockNormal(block: (String) -> Boolean) {
//                        // 处理 正常的struct
//                        val jsonObject = tryGetJSON<JSONObject>(json)
//                        val itemJsonObject = jsonObject?.getJSONObject(sIdentifier) ?: return
//                        block.invoke(itemJsonObject.toString())
//                    }
//
//                    val itemValues = this.itemValues
//                    val sMap: MutableMap<PropertyKey, PropertyValue<*>> = itemValues.toMutableMap()
//                    itemValues.forEach { sItem ->
//                        val sPkIdentifier = sItem.key
//                        val sValue = sItem.value
//                        findIdentifierWithBlockNormal {
//                            val fromJson = sValue.tslHandleUpdatePropertyValueFromJson(it)
//                            if (fromJson != null) {
//                                sMap[sPkIdentifier] = fromJson
//                                true
//                            } else {
//                                false
//                            }
//                        }
//                    }
//                    StructPropertyValue(sPropertyKey, sMap)
//                }
//            } catch (e: Throwable) {
//                e.printStackTrace()
//                DefaultLogger.ld("struct error $e}")
//                null
//            } finally {
//            }
//        }
//
//        is StructArrayPropertyValue -> {
//            try {
//                // 更新array<struct>数据
//                val sPropertyKey = this.key
//                val sIdentifier = sPropertyKey.identifier
//
//                tryGetJSON<JSONObject>(json)?.let { jsonObject ->
//                    val jsonArray = jsonObject.getJSONArray(sIdentifier)
//                    DefaultLogger.ld("### 更新array<struct>数据 jsonArray : $jsonArray")
//                    // [{"twins_array_a":0,"twins_array_b":false}]
//                    val list: MutableList<StructPropertyValue> = mutableListOf()
//                    val length = jsonArray.length()
//                    if (length > 0) {
//                        (0 until length).forEach {
//                            try {
//                                val structJson = jsonArray.getJSONObject(it).toString()
//                                DefaultLogger.ld("###      structJson = $structJson")
//                                val structValue = sPropertyKey.defaultItemValue()
//                                    .tslHandleUpdatePropertyValueFromJson(structJson)
//                                DefaultLogger.ld("### structValue = $structValue")
//                                if (structValue != null && structValue is StructPropertyValue) {
//                                    list.add(structValue)
//                                }
//                            } catch (e: Throwable) {
//                                e.printStackTrace()
//                            }
//                        }
//                    }
//
//                    StructArrayPropertyValue(
//                        sPropertyKey,
//                        list
//                    )
//                }
////                tryGetJSON<JSONObject>(json)?.let { jsonObject ->
////                    val jsonArray = jsonObject.getJSONArray(sIdentifier)
////                    DefaultLogger.ld("### 更新array<struct>数据 jsonArray : $jsonArray")
////                    val length = jsonArray.length()
////                    if (length > 0) {
////                        (0 until length).forEach {
////                            try {
////                                val structJson = jsonArray.getJSONObject(it).toString()
////                                DefaultLogger.ld("###      structJson = $structJson")
////                                val structValue = tslHandleUpdatePropertyValueFromJson(structJson)
////                                DefaultLogger.ld("### structValue = $structValue")
////                                if (structValue != null && structValue is StructPropertyValue) {
////                                    list.add(structValue)
////                                }
////                            } catch (e: Throwable) {
////                                e.printStackTrace()
////                            }
////                        }
////                    }
////                    StructArrayPropertyValue(
////                        sPropertyKey,
////                        list
////                    )
////                }
//
//            } catch (e: Throwable) {
//                e.printStackTrace()
//                DefaultLogger.le("### error $e")
//                null
//            }
//        }
//    }.also {
//        logger.ld("    tslHandleUpdatePropertyValueFromJson end:${it?.displayValue}")
//    }
}

/**
 * 解析Tsl 转化成分类的PropertyKey的集合
 *
 * @return 分类好的PropertyKey的集合map key为物的属性id，值为解析好的分类的PropertyKey
 */
fun Tsl.tslHandleParsePropertyKeys(): Map<String, PropertyKey> {
    return try {
        logger.ld("tslHandleParsePropertyKeys a")
        val properties = properties
        if (properties.isEmpty()) return mapOf()
        val all: MutableMap<String, PropertyKey> = mutableMapOf()
        properties.forEach { tslProperty ->
            val id = tslProperty.identifier
            val key = tslHandleParsePropertyKey(tslProperty)
            if (key != null) {
                all[id] = key
            }
        }
        all
    } catch (e: Throwable) {
        e.printStackTrace()
        logger.ld("tslHandleParsePropertyKeys error $e")
        mapOf()
    } finally {
        logger.ld("tslHandleParsePropertyKeys z")
    }
}

internal val logger = DefaultLogger("_tsl_")