plugins {
    id("pixabayimages.android.library")
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "com.semba.pixabayimages.data.model"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(libs.kotlinx.serialization)
}
