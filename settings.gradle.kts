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
include(":external:auth")
include(":common")
include(":external:ComposeComponents")
include(":model")
include (":external:mapLocation")
include(":core:database")
include(":core:workManager")
include(":core:data")
