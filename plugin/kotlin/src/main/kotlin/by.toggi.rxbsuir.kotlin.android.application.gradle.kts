plugins {
    id("by.toggi.rxbsuir.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jlleitschuh.gradle.ktlint")
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
    sourceSets.configureEach {
        java.srcDirs("src/$name/kotlin")
    }
}

dependencies {
    testImplementation(kotlin("test-junit"))
    androidTestImplementation(kotlin("test-junit"))
}
