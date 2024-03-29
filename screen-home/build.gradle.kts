@file:Suppress("DSL_SCOPE_VIOLATION", "UnstableApiUsage")

plugins {
    id("by.toggi.rxbsuir.kotlin.android.library")
    id("by.toggi.rxbsuir.kotlin.kapt")
}

android {
    namespace = "by.toggi.rxbsuir.screen.home"
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(projects.workflow)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)

    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)

    testImplementation(projects.workflowTest)
}
