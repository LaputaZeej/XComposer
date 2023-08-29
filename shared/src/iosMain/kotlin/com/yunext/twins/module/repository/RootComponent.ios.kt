package com.yunext.twins.module.repository

import com.yunext.twins.module.db.DatabaseDriverFactory

actual fun createRootComponent(): RootComponent{
    return RootComponent(DatabaseDriverFactory())
}