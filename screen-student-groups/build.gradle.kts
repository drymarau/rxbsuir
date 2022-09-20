@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id("by.toggi.rxbsuir.kotlin.android.library")
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "by.toggi.rxbsuir.screen.studentgroups"
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(projects.api)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.workflow.ui.compose)

    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)

    testImplementation(libs.workflow.testing.jvm)
}
