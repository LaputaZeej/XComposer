package com.yunext.twins.module.db

import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import com.yunext.kmm.twins.shared.cache.AppDatabase

actual class DatabaseDriverFactory() {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(AppDatabase.Schema, DBConstant.DB_NAME)
    }

}

