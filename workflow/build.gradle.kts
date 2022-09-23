@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id("by.toggi.rxbsuir.kotlin.android.library")
}

android {
    namespace = "by.toggi.rxbsuir.workflow"
    buildFeatures {
        compose = true
    }
}

dependencies {
    api(libs.androidx.compose.runtime)
}
