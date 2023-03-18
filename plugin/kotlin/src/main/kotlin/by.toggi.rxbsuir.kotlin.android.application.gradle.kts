@file:Suppress("UnstableApiUsage")

plugins {
    id("by.toggi.rxbsuir.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jlleitschuh.gradle.ktlint")
}

android {
    sourceSets.configureEach {
        java.srcDirs("src/$name/kotlin")
    }
}

dependencies {
    testImplementation(kotlin("test-junit"))
    androidTestImplementation(kotlin("test-junit"))
}

tasks.withType<Test>().configureEach {
    systemProperty("kotlinx.coroutines.stacktrace.recovery", false)
}
