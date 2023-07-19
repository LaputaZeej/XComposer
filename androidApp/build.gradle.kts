println("∆∆∆∆[:androidApp/build.gradle.kts]")
plugins {
    println("∆∆∆∆[:androidApp/build.gradle.kts]->plugins")
    kotlin("multiplatform")
    id("com.android.application")
    id("org.jetbrains.compose")
}

kotlin {
    println("∆∆∆∆[:androidApp/build.gradle.kts]->kotlin")
    android()
    sourceSets {
        val androidMain by getting {
            dependencies {
                implementation(project(":shared"))
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.material3) // 3
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
                implementation(compose.uiTooling)
                implementation(compose.preview)
                implementation(compose.ui)

                implementation("androidx.activity:activity-compose:1.7.2")
                implementation("androidx.appcompat:appcompat:1.6.1")
                implementation("androidx.core:core-ktx:1.10.1")
                implementation("androidx.navigation:navigation-runtime-ktx:2.6.0")
            }
        }
    }
}

android {
    println("∆∆∆∆[:androidApp/build.gradle.kts]->android")
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = (findProperty("twins.application.id") as String)
    println("∆∆∆∆namespace      =   $namespace")
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")

    defaultConfig {
        val aid = findProperty("twins.application.id") as String
        applicationId = aid
        minSdk = (findProperty("android.minSdk") as String).toInt()
        targetSdk = (findProperty("android.targetSdk") as String).toInt()
        versionCode = 1
        versionName = "1.0"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        jvmToolchain(11)
    }

//    buildFeatures {
//        compose = true
//    }
//
//    composeOptions {
//        kotlinCompilerExtensionVersion ="1.3.2"
//    }
//    packagingOptions {
//        resources {
//            excludes += "/META-INF/{AL2.0,LGPL2.1}"
//        }
//    }

}

//dependencies {
//    implementation(project(":shared"))
//    implementation(compose.runtime)
//    implementation(compose.foundation)
//    implementation(compose.material)
//    implementation(compose.material3) // 3
//    @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
//    implementation(compose.components.resources)
//
//    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
//    implementation(compose.uiTooling)
//    implementation(compose.preview)
//    implementation(compose.ui)
//
//    implementation("androidx.activity:activity-compose:1.6.1")
//    implementation("androidx.appcompat:appcompat:1.6.1")
//    implementation("androidx.core:core-ktx:1.9.0")
//    implementation("androidx.navigation:navigation-runtime-ktx:2.6.0")
//}

println("∆∆∆∆[:androidApp/build.gradle.kts]end")
println("∆∆∆∆")
