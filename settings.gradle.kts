pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    includeBuild("build-logic")
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
rootProject.name = "MedicalService"
include(":app")
include(":auth")
include(":common")
include(":ComposeComponents")
include(":model")
include (":mapLocation")