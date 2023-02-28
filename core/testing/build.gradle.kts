plugins {
    id("pixabayimages.android.library")
    id("pixabayimages.android.hilt")
    alias(libs.plugins.kotlinx.serialization)
}

android {

    namespace = "com.semba.pixabayimages.core.testing"
}

dependencies {

    api(project(":core:common"))
    api(project(":data:model"))
    api(project(":data:local"))
    api(project(":data:remote"))
    api(project(":data:repository"))

    api(libs.kotlinx.serialization)

    api(libs.junit4)
    api(libs.androidx.test.core)
    api(libs.kotlinx.coroutines.test)
    api(libs.turbine)

    api(libs.androidx.espresso.core)
    api(libs.androidx.test.runner)
    api(libs.androidx.test.rules)
    api(libs.androidx.compose.ui.test)
    api(libs.hilt.android.testing)
    api(libs.okhttp3)
    api(libs.mockk)
    api(libs.mock.webserver)

    debugApi(libs.androidx.compose.ui.testManifest)
}