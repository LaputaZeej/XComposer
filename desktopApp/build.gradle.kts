import org.jetbrains.compose.desktop.application.dsl.TargetFormat
println("∆∆∆∆[:desktopApp/build.gradle.kts]")
plugins {
    println("∆∆∆∆[:desktopApp/build.gradle.kts]->plugins")
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    println("∆∆∆∆[:desktopApp/build.gradle.kts]->kotlin")
    jvm()
    sourceSets {
        println("∆∆∆∆[:desktopApp/build.gradle.kts]->sourceSets")
        val jvmMain by getting  {
            dependencies {
                implementation(compose.desktop.currentOs)
                // <!-- https://mvnrepository.com/artifact/org.bouncycastle/bcpkix-jdk15on -->

                implementation("org.bouncycastle:bcpkix-jdk15on:1.70")
                implementation(project(":shared"))
            }
        }
    }
}

compose.desktop {
    println("∆∆∆∆[:desktopApp/build.gradle.kts]->compose.desktop")
    application {
        println("∆∆∆∆[:desktopApp/build.gradle.kts]->compose.desktop->application")
        mainClass = "MainKt"

        nativeDistributions {
            println("∆∆∆∆[:desktopApp/build.gradle.kts]->compose.desktop->application->nativeDistributions")
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "KotlinMultiplatformComposeDesktopApplication"
            packageVersion = "1.0.0"
            println("∆∆∆∆packageName       =   $packageName")
            println("∆∆∆∆packageVersion    =   $packageVersion")
        }
    }
}
println("∆∆∆∆[:desktopApp/build.gradle.kts] end")
println("∆∆∆∆")
