package com.yunext.twins.module.db

import com.squareup.sqldelight.db.SqlDriver

expect class DatabaseDriverFactory {
    fun createDriver():SqlDriver
}

//expect fun providerDatabaseDriverFactory():DatabaseDriverFactory