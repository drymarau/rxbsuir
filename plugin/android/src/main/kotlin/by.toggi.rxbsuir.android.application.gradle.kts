@file:Suppress("UnstableApiUsage")

import gradle.kotlin.dsl.accessors._80a2ae57395e1362b61311ead0eb480f.java

plugins {
    id("com.android.application")
}

android {
    compileSdk = Android.compileSdk
    defaultConfig {
        minSdk = Android.minSdk
        targetSdk = Android.targetSdk
        testInstrumentationRunner = Android.testInstrumentationRunner
    }
    buildFeatures {
        buildConfig = true
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
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
    lint {
        checkDependencies = true
        ignoreTestSources = true
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
