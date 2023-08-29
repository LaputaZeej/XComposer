package com.yunext.twins.module.db.datasource

import com.yunext.twins.shared.cache.News


interface NewsDatasource {

    fun list(): List<News>
    fun add(news: News)
    fun clear()
    fun addAll(list:List<News>)
}