@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    id("by.toggi.rxbsuir.kotlin.jvm")
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    api(libs.okhttp)
    api(libs.kotlinx.datetime)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.json.okio)
    implementation(libs.dagger.hilt.core)
    kapt(libs.dagger.hilt.compiler)

    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.okhttp.mockwebserver)
}
