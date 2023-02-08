plugins {
    id("pixabayimages.android.library")
    id("pixabayimages.android.library.compose")
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    lint {
        checkDependencies = true
    }
    namespace = "com.semba.pixabayimages.core.design"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    api(libs.androidx.compose.ui)
    api(libs.androidx.compose.material3)
    debugApi(libs.androidx.compose.ui.tooling)
    api(libs.androidx.compose.ui.tooling.preview)
    api(libs.androidx.compose.runtime)
}