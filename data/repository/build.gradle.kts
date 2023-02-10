plugins {
    id("pixabayimages.android.library")
    id("pixabayimages.android.hilt")
    alias(libs.plugins.kotlinx.serialization)
}

android {
    namespace = "com.semba.pixabayimages.data.repository"
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(project(":core:common"))
    implementation(project(":data:model"))
    implementation(project(":data:local"))
    implementation(project(":data:remote"))

    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.serialization)
}