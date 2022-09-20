@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id("by.toggi.rxbsuir.kotlin.android.library")
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "by.toggi.rxbsuir.screen.home"
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.workflow.ui.compose)

    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)

    testImplementation(libs.workflow.testing.jvm)
}
