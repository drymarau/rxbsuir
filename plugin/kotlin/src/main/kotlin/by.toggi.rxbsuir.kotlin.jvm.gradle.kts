plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jlleitschuh.gradle.ktlint")
    id("com.android.lint")
}

kotlin {
    explicitApi()
    jvmToolchain(11)
}

dependencies {
    testImplementation(kotlin("test-junit"))
}

tasks.withType<Test>().configureEach {
    testLogging.exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
    systemProperty("kotlinx.coroutines.stacktrace.recovery", false)
}
