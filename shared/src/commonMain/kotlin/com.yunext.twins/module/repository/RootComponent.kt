package com.yunext.twins.module.repository

import com.yunext.twins.module.db.DatabaseDriverFactory
import com.yunext.twins.module.db.datasource.impl.NewsDatasourceImpl
import com.yunext.twins.module.repository.impl.NewsRepositoryImpl
import com.yunext.twins.ui.page.debug.NewsDialogStore

class RootComponent(
    sqlDriverFactory: DatabaseDriverFactory,
) {
     val newsRepository: NewsRepository =
        NewsRepositoryImpl(NewsDatasourceImpl(sqlDriverFactory))

    companion object {
        val rootComponent: RootComponent by lazy {
            createRootComponent()
        }
    }
}

expect fun createRootComponent(): RootComponent