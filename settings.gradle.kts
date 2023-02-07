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
        mavenCentral()
    }
}

rootProject.name = "PixabayImages"
include(":app")
include(":feature:searchscreen")
include(":feature:detailscreen")
include(":data:remote")
include(":data:model")
include(":data:repository")
include(":data:local")
