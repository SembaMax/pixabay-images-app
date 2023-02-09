plugins {
    id("pixabayimages.android.library")
    id("pixabayimages.android.hilt")
}

android {
    namespace = "com.semba.pixabayimages.core.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
}