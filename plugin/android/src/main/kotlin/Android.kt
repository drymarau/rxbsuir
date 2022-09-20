import org.gradle.api.JavaVersion

internal object Android {

    const val minSdk = 23
    const val targetSdk = 33
    const val compileSdk = 33
    const val testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

    val sourceCompatibility = JavaVersion.VERSION_11
    val targetCompatibility = JavaVersion.VERSION_11
}
