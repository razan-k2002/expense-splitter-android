pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()                     // keep this
        maven("https://jitpack.io")        // required for MPAndroidChart
    }
}

rootProject.name = "PersonalExpenseSplitterProject"
include(":app")
