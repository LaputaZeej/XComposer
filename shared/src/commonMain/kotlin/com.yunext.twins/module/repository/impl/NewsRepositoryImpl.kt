package com.yunext.twins.module.repository.impl

import com.yunext.twins.module.db.datasource.NewsDatasource
import com.yunext.twins.module.repository.NewsRepository
import com.yunext.twins.shared.cache.News
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

class NewsRepositoryImpl(
    private val newsDatasource: NewsDatasource,
) : NewsRepository {
    override suspend fun list(): List<News> {
        return suspendCancellableCoroutine {

            try {
                it.resumeWith(Result.success(newsDatasource.list()))
            } catch (e: Exception) {
                it.resumeWithException(e)
            }
            it.invokeOnCancellation {

            }
        }

    }

    override suspend fun add(news: News) {
        return suspendCancellableCoroutine {

            try {
                it.resumeWith(Result.success(newsDatasource.add(news)))
            } catch (e: Exception) {
                it.resumeWithException(e)
            }
            it.invokeOnCancellation {

            }
        }
    }

    override suspend fun clear() {
        return suspendCancellableCoroutine {

            try {
                it.resumeWith(Result.success(newsDatasource.clear()))
            } catch (e: Exception) {
                it.resumeWithException(e)
            }
            it.invokeOnCancellation {

            }
        }
    }

    override suspend fun addAll(list: List<News>) {
        return suspendCancellableCoroutine {
            try {
                it.resumeWith(Result.success(newsDatasource.addAll(list)))
            } catch (e: Exception) {
                it.resumeWithException(e)
            }
            it.invokeOnCancellation {

            }
        }
    }
}