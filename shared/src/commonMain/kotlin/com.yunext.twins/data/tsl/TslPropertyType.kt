package com.yunext.twins.data.tsl

/*
    * DataTypeEnum
    * 属性类型
    * int
    * float
    * double
    * text
    * date（String类型UTC毫秒）
    * bool（0或1的int类型）
    * enum（int类型，枚举项定义方法与bool类型定义0和1的值方法相同）
    * struct（结构体类型，可包含前面7种类型，下面使用"specs":[{}]描述包含的对象）
    * array（数组类型，支持int、double、float、text、struct）
    */
enum class TslPropertyType( val text: String) {
    INT("int"),
    FLOAT("float"),
    DOUBLE("double"),
    TEXT("text"),
    DATE("date"),
    BOOL("bool"),
    ENUM("enum"),
    STRUCT("struct"),
    ARRAY("array"),
    ;

    companion object{
        fun from(text: String): TslPropertyType {
            return when(text){
                INT.text-> INT
                FLOAT.text-> FLOAT
                DOUBLE.text-> DOUBLE
                TEXT.text-> TEXT
                DATE.text-> DATE
                BOOL.text-> BOOL
                ENUM.text-> ENUM
                STRUCT.text-> STRUCT
                ARRAY.text-> ARRAY
                else-> throw TslException("不支持的PropertyType:$text")
            }
        }
    }
}