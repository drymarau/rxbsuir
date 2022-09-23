@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id("by.toggi.rxbsuir.kotlin.android.application")
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.parcelize)
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
    buildFeatures {
        compose = true
    }
}

hilt {
    enableAggregatingTask = true
}

dependencies {
    implementation(projects.screenHome)
    implementation(projects.screenStudentGroups)
    implementation(projects.workflow)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.compose.material3)
    implementation(libs.accompanist.systemuicontroller)

    implementation(libs.okhttp)
    implementation(libs.okhttp.logginginterceptor)

    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)
}
