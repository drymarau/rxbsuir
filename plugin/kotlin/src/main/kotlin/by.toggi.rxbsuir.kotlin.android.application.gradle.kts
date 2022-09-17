plugins {
    id("by.toggi.rxbsuir.android.application")
    id("org.jetbrains.kotlin.android")
}

pluginManager.withPlugin("org.jetbrains.kotlin.kapt") {
    configure<org.jetbrains.kotlin.gradle.plugin.KaptExtension> {
        correctErrorTypes = true
    }
}

android {
    kotlinOptions {
        jvmTarget = "${compileOptions.targetCompatibility}"
    }
}

dependencies {
    testImplementation(kotlin("test-junit"))
    androidTestImplementation(kotlin("test-junit"))
}
