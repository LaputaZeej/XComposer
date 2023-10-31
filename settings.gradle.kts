rootProject.name = "XComposer"

include(":androidApp")
include(":shared")
include(":desktopApp")

println("∆∆∆∆[settings.gradle.kts]")

pluginManagement {
    println("∆∆∆∆[settings.gradle.kts]->pluginManagement")

    repositories {
        println("∆∆∆∆[settings.gradle.kts]->pluginManagement->repositories")
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
    }

    plugins {
        println("∆∆∆∆[settings.gradle.kts]->pluginManagement->plugins")
        val kotlinVersion = extra["kotlin.version"] as String
        val agpVersion = extra["agp.version"] as String
        val composeVersion = extra["compose.version"] as String
        val yunextVersion = extra["com.yunext.version"] as String
        val sqlDelightVersion = extra["sqlDelightVersion"] as String
        println("∆∆∆∆<<<<<<<<< yunextVersion = $yunextVersion >>>>>>>>>")



        kotlin("jvm").version(kotlinVersion)
        kotlin("multiplatform").version(kotlinVersion)
        kotlin("android").version(kotlinVersion)

        id("com.android.application").version(agpVersion)
        id("com.android.library").version(agpVersion)

        id("org.jetbrains.compose").version(composeVersion)

        kotlin("plugin.serialization") version(kotlinVersion)
        id("com.squareup.sqldelight") version(sqlDelightVersion)
        id("org.jetbrains.kotlin.jvm") version "1.8.20"
    }
}

dependencyResolutionManagement {
    println("∆∆∆∆[settings.gradle.kts]->dependencyResolutionManagement")
    repositories {
        println("∆∆∆∆[settings.gradle.kts]->dependencyResolutionManagement->repositories")
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
println("∆∆∆∆[settings.gradle.kts] end")
println("∆∆∆∆")


