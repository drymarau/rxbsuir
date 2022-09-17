@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id("by.toggi.rxbsuir.kotlin.android.application")
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.kotlin.kapt)
}

// Manifest version information
val versionMajor = 1
val versionMinor = 4
val versionPatch = 5
val versionBuild = 0

android {
    namespace = "by.toggi.rxbsuir"
    defaultConfig {
        applicationId = "by.toggi.rxbsuir"
        versionCode = versionMajor * 10000 + versionMinor * 1000 + versionPatch * 100 + versionBuild
        versionName = "$versionMajor.$versionMinor.$versionPatch"
    }
}

hilt {
    enableAggregatingTask = true
}

dependencies {
    implementation(libs.androidx.activity)

    implementation(libs.dagger.hilt.android.runtime)
    kapt(libs.dagger.hilt.compiler)
}
