plugins {
    kotlin("jvm")
    id("org.jlleitschuh.gradle.ktlint")
    id("com.android.lint")
}

pluginManager.withPlugin("org.jetbrains.kotlin.kapt") {
    configure<org.jetbrains.kotlin.gradle.plugin.KaptExtension> {
        correctErrorTypes = true
    }
}

kotlin {
    explicitApi()
    jvmToolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

dependencies {
    testImplementation(kotlin("test-junit"))
}

tasks.withType<Test>().configureEach {
    testLogging.exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    systemProperty("kotlinx.coroutines.stacktrace.recovery", false)
}
