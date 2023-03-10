pluginManagement {
    includeBuild("build-logic")
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
        mavenCentral()
    }
}

rootProject.name = "pixabayimages"
include(":app")
include(":feature:searchscreen")
include(":feature:detailscreen")
include(":data:remote")
include(":data:model")
include(":data:repository")
include(":data:local")
include(":core:common")
include(":core:design")
include(":core:testing")
