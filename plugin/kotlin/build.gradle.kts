plugins {
    `kotlin-dsl`
}

kotlin {
    jvmToolchain(11)
}

dependencies {
    implementation(project(":android"))

    implementation(libs.kotlin)
    implementation(libs.ktlint)
}
