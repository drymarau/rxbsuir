plugins {
    `kotlin-dsl`
}

kotlin {
    jvmToolchain {
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(11))
    }
}

dependencies {
    implementation(project(":android"))
    implementation(libs.kotlin)
}
