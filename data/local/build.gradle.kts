@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("pixabayimages.android.library")
    id("pixabayimages.android.hilt")
    alias(libs.plugins.ksp)
}

android {
    defaultConfig {
        // The schemas directory contains a schema file for each version of the Room database.
        // This is required to enable Room auto migrations.
        // See https://developer.android.com/reference/kotlin/androidx/room/AutoMigration.
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }

    }
    namespace = "com.semba.pixabayimages.data.local"
}

dependencies {

    implementation(project(":data:model"))

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    implementation(libs.kotlinx.coroutines.android)
}