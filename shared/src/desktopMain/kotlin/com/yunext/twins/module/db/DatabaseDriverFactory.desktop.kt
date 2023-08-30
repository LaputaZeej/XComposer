package com.yunext.twins.module.db

import com.squareup.sqldelight.db.SqlDriver

import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import com.yunext.kmm.twins.shared.cache.AppDatabase
import java.io.File

actual class DatabaseDriverFactory() {
    actual fun createDriver(): SqlDriver {
//        val databasePath = File(System.getProperty(PROPERTY, DBConstant.DB_NAME))
//        println("DatabaseDriverFactory databasePath:$databasePath - ${databasePath.exists()}")
        val file = File(DBConstant.DB_NAME)
        val driver = JdbcSqliteDriver("jdbc:sqlite:${DBConstant.DB_NAME}")
//        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        println("DatabaseDriverFactory driver:$driver")
        // https://slack-chats.kotlinlang.org/t/497321/i-am-trying-to-use-sqldelight-on-a-compose-for-desktop-app-i
        // Are you calling the create and migrate methods on the generated schema
        //        It’s only android where that’s done automatically, everywhere else you have to do it yourself
        if (!file.exists()){
            println("DatabaseDriverFactory create driver $file")
            AppDatabase.Schema.create(driver)
        }else{
            println("DatabaseDriverFactory created $file")
        }
        return driver
    }

    companion object {
        private const val PROPERTY = "java.io.tmpdir"
    }

}

