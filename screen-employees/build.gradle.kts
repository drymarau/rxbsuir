@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id("by.toggi.rxbsuir.kotlin.android.library")
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "by.toggi.rxbsuir.screen.employees"
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(projects.api)
    implementation(projects.workflow)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.coil.compose)

    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.hilt.compiler)

    testImplementation(projects.workflowTest)
}
