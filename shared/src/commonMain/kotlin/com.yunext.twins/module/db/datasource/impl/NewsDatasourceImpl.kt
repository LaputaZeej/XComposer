package com.yunext.twins.module.db.datasource.impl

import com.yunext.kmm.twins.shared.cache.AppDatabase
import com.yunext.twins.module.db.DatabaseDriverFactory
import com.yunext.twins.module.db.datasource.NewsDatasource
import com.yunext.twins.shared.cache.AppDatabseQueries
import com.yunext.twins.shared.cache.News

class NewsDatasourceImpl(sqlDriverFactory: DatabaseDriverFactory) : NewsDatasource {
    private val database = AppDatabase(sqlDriverFactory.createDriver())

    private val dbQuery = database.appDatabseQueries
    override fun list(): List<News> {
        return dbQuery.selectAllNewsInfo(::mapLaunchSelecting).executeAsList()
    }

    override fun add(news: News) {
        dbQuery.transaction {
            dbQuery.insertSingle(news)
        }
    }

    override fun clear() {
        dbQuery.transaction {
            dbQuery.removeAllNews()
        }
    }

    override fun addAll(list: List<News>) {
        dbQuery.transaction {
            list.forEach {
                news ->
                dbQuery.insertSingle(news)
            }
        }
    }

    private fun AppDatabseQueries.insertSingle(news: News){
        insertNews(news.id,news.title,news.summary,news.date,news.imageUrl)
    }

    private fun mapLaunchSelecting(
        id: Long,
        title: String,
        summary: String?,
        date: String,
        imageUrl: String?,
    ): News {
        return News(
            id = id,
            title = title,
            summary = summary,
            date = date,
            imageUrl = imageUrl

        )
    }
}