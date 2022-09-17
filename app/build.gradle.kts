@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.dagger.hilt.android)
}

// Manifest version information
val versionMajor = 1
val versionMinor = 4
val versionPatch = 5
val versionBuild = 0

android {
    namespace = "by.toggi.rxbsuir"
    compileSdk = 33
    defaultConfig {
        applicationId = "by.toggi.rxbsuir"
        minSdk = 21
        targetSdk = 33
        versionCode = versionMajor * 10000 + versionMinor * 1000 + versionPatch * 100 + versionBuild
        versionName = "${versionMajor}.${versionMinor}.${versionPatch}"

        resourceConfigurations += setOf("en", "ru")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    testOptions {
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
    }
}

hilt {
    enableAggregatingTask = true
}

dependencies {
    coreLibraryDesugaring(libs.android.desugar)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.preference)
    implementation(libs.androidx.recyclerview)

    implementation(libs.material)

    implementation(libs.dagger.hilt.android.runtime)
    annotationProcessor(libs.dagger.hilt.compiler)
}
