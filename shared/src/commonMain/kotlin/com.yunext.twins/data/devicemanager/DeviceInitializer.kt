package com.yunext.twins.data.devicemanager

import com.yunext.twins.data.tsl.property.tslHandleTsl2PropertyValues
import com.yunext.twins.data.tsl.property.tslHandleUpdatePropertyValues
import com.yunext.twins.data.tsl.property.tslHandleUpdatePropertyValuesFromJson
import com.yunext.twins.data.tsl.Tsl
import com.yunext.twins.data.tsl.property.PropertyValue

interface DeviceInitializer {
    fun init(map: Map<String, PropertyValue<*>>):Map<String, PropertyValue<*>>
}

class JsonDeviceInitializer(val json:String):DeviceInitializer{
    override fun init(map: Map<String, PropertyValue<*>>): Map<String, PropertyValue<*>> {
        return  tslHandleUpdatePropertyValuesFromJson(map, json).first
    }
}

class PropertyValuesDeviceInitializer(val  list :List<PropertyValue<*>>):DeviceInitializer{
    override fun init(map: Map<String, PropertyValue<*>>): Map<String, PropertyValue<*>> {
        return  tslHandleUpdatePropertyValues(map, list)
    }
}

class TslDeviceInitializer(val  tsl : Tsl):DeviceInitializer{
    override fun init(map: Map<String, PropertyValue<*>>): Map<String, PropertyValue<*>> {
        return  tsl.tslHandleTsl2PropertyValues()
    }
}