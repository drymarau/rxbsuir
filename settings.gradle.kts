@file:Suppress("UnstableApiUsage")

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
    includeBuild("plugin")
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        google()
    }
}

rootProject.name = "rxbsuir"
include(":api")
include(":app")
include(":screen-announcements")
include(":screen-employees")
include(":screen-home")
include(":screen-student-groups")
include(":workflow")
include(":workflow-test")
