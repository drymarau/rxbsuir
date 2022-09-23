@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id("by.toggi.rxbsuir.kotlin.android.library")
}

android {
    namespace = "by.toggi.rxbsuir.workflow.test"
    buildFeatures {
        compose = true
    }
}

dependencies {
    api(projects.workflow)
    api(kotlin("test"))
    api(libs.kotlinx.coroutines.test)
    api(libs.molecule)
    api(libs.turbine)
}
