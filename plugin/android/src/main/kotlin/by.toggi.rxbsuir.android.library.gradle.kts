@file:Suppress("UnstableApiUsage")

plugins {
    id("com.android.library")
}

android {
    compileSdk = Android.compileSdk
    defaultConfig {
        minSdk = Android.minSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures {
        buildConfig = false
    }
    compileOptions {
        sourceCompatibility = Android.sourceCompatibility
        targetCompatibility = Android.targetCompatibility
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Android.kotlinCompilerExtensionVersion
    }
    testOptions {
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

tasks.withType<Test>().configureEach {
    testLogging.exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
}
