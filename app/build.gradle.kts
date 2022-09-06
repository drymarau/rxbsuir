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
    compileSdk = 28

    defaultConfig {
        applicationId = "by.toggi.rxbsuir"
        minSdk = 21
        targetSdk = 28
        versionCode = versionMajor * 10000 + versionMinor * 1000 + versionPatch * 100 + versionBuild
        versionName = "${versionMajor}.${versionMinor}.${versionPatch}"

        resourceConfigurations += setOf("en", "ru")

        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    testOptions {
        execution = "ANDROID_TEST_ORCHESTRATOR"
    }
}

dependencies {
    coreLibraryDesugaring(libs.android.desugar)

    implementation(libs.android.support.appcompat)
    implementation(libs.android.support.cardview)
    implementation(libs.android.support.design)
    implementation(libs.android.support.recyclerview)

    implementation(libs.butterknife)
    annotationProcessor(libs.butterknife.compiler)

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

    implementation(libs.storio)
    implementation(libs.storio.annotations)
    annotationProcessor(libs.storio.annotations.processor)

    implementation(libs.dagger)
    implementation(libs.dagger.android.legacy)
    implementation(libs.dagger.android.support.legacy)
    annotationProcessor(libs.dagger.compiler)
    annotationProcessor(libs.dagger.android.processor)

    implementation(libs.parceler.api)
    annotationProcessor(libs.parceler)

    testImplementation(libs.junit)

    testImplementation(libs.okhttp.mockwebserver)

    testImplementation(libs.mockito)

    testImplementation(libs.assertj)
}
