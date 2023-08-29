package com.yunext.twins.module.repository

import com.yunext.twins.shared.cache.News

interface NewsRepository {
    suspend fun list(): List<News>
    suspend fun add(news: News)
    suspend fun clear()
    suspend fun addAll(list:List<News>)
}