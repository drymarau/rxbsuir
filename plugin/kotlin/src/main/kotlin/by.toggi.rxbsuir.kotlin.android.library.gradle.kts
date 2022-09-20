@file:Suppress("UnstableApiUsage")

plugins {
    id("by.toggi.rxbsuir.android.library")
    id("org.jetbrains.kotlin.android")
    id("org.jlleitschuh.gradle.ktlint")
}

pluginManager.withPlugin("org.jetbrains.kotlin.kapt") {
    configure<org.jetbrains.kotlin.gradle.plugin.KaptExtension> {
        correctErrorTypes = true
    }
}

android {
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.1"
    }
    kotlinOptions {
        jvmTarget = "${compileOptions.targetCompatibility}"
        freeCompilerArgs += "-Xexplicit-api=strict"
    }
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
