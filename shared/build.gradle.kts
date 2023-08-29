println("∆∆∆∆[:shared/build.gradle.kts]")
plugins {
    println("∆∆∆∆[:shared/build.gradle.kts]->plugins")
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("org.jetbrains.compose")
    kotlin("plugin.serialization")
    id("com.squareup.sqldelight")
}

kotlin {
    println("∆∆∆∆[:shared/build.gradle.kts]->kotlin")
    android()

    jvm("desktop")

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        println("∆∆∆∆[:shared/build.gradle.kts]->kotlin->cocoapods")
        version = "1.0.0"
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
        }
        extraSpecAttributes["resources"] =
            "['src/commonMain/resources/**', 'src/iosMain/resources/**']"
    }

    sourceSets {
        println("∆∆∆∆[:shared/build.gradle.kts]->kotlin->sourceSets")
        val commonMain by getting {
            dependencies {

                implementation(compose.ui)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.material3) // 3
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.1")
                implementation("com.squareup.sqldelight:runtime:1.5.5")
            }
        }
        val androidMain by getting {
            println("∆∆∆∆[:shared/build.gradle.kts]->kotlin->sourceSets->androidMain")
            dependencies {
                implementation("androidx.activity:activity-compose:1.6.1")
                implementation("androidx.appcompat:appcompat:1.6.1")
                implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.3.1")
                implementation("androidx.navigation:navigation-runtime-ktx:2.5.3")
                implementation("androidx.navigation:navigation-compose:2.5.3")
                implementation("androidx.appcompat:appcompat:1.6.1")
                implementation("androidx.lifecycle:lifecycle-process:2.6.1")
                implementation("androidx.compose.animation:animation-graphics:1.4.3")
                implementation("androidx.core:core-ktx:1.9.0")
                implementation("androidx.navigation:navigation-runtime-ktx:2.6.0")
                implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")

                implementation("com.squareup.sqldelight:android-driver:1.5.5")
                implementation(compose.uiTooling)
                implementation(compose.preview)
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            println("∆∆∆∆[:shared/build.gradle.kts]->kotlin->sourceSets->iosMain")
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies{
                implementation("com.squareup.sqldelight:native-driver:1.5.5")
            }

        }
        val desktopMain by getting {
            println("∆∆∆∆[:shared/build.gradle.kts]->kotlin->sourceSets->desktopMain")
            dependencies {

                implementation("com.squareup.sqldelight:sqlite-driver:1.5.5")
                implementation(compose.desktop.common)
            }
        }
    }
}

android {
    println("∆∆∆∆[:shared/build.gradle.kts]->android")
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    val aid = findProperty("twins.application.id") as String
    namespace = aid

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
        targetSdk = (findProperty("android.targetSdk") as String).toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        jvmToolchain(11)
    }
}

println("∆∆∆∆[:shared/build.gradle.kts] end")
println("∆∆∆∆")

sqldelight {
    database("AppDatabase") {
        packageName = "com.yunext.kmm.twins.shared.cache"
    }
}

