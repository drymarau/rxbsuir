@file:Suppress("DSL_SCOPE_VIOLATION")

plugins {
    alias(libs.plugins.android.application)
}

// Manifest version information
val versionMajor = 1
val versionMinor = 4
val versionPatch = 5
val versionBuild = 0

android {
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

dependencies {
    coreLibraryDesugaring(libs.android.desugar)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.recyclerview)

    implementation(libs.material)

    implementation(libs.timber)

    implementation(libs.rxbinding)
    implementation(libs.rxbinding.appcompat)

    implementation(libs.okhttp)
    implementation(libs.okhttp.logginginterceptor)

    implementation(libs.retrofit)
    implementation(libs.retrofit.adapter.rxjava)
    implementation(libs.retrofit.converter.gson)

    implementation(libs.rxjava)
    implementation(libs.rxandroid)

    implementation(libs.rxlifecycle)
    implementation(libs.rxlifecycle.components)

    implementation(libs.rxpreferences)

    implementation(libs.materialdialogs)
    implementation(libs.materialdialogs.commons)

    implementation(libs.preference)
    implementation(libs.preference.datetimepicker)

    implementation(libs.dagger)
    implementation(libs.dagger.android)
    implementation(libs.dagger.android.support)
    annotationProcessor(libs.dagger.compiler)
    annotationProcessor(libs.dagger.android.processor)

    implementation(libs.parceler.api)
    annotationProcessor(libs.parceler)

    testImplementation(libs.junit)

    testImplementation(libs.okhttp.mockwebserver)

    testImplementation(libs.mockito)

    testImplementation(libs.assertj)
}
