package com.yunext.twins.module.db

import com.squareup.sqldelight.db.SqlDriver

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import java.io.File

actual class DatabaseDriverFactory() {
    actual fun createDriver(): SqlDriver {
//        val databasePath = File(System.getProperty(PROPERTY, DBConstant.DB_NAME))
//        println("DatabaseDriverFactory databasePath:$databasePath")
//        val driver = JdbcSqliteDriver("${JdbcSqliteDriver.IN_MEMORY}${databasePath.absolutePath}")
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        println("DatabaseDriverFactory driver:$driver")
        return driver
    }

    companion object {
        private const val PROPERTY = "java.io.tmpdir"
    }

}

