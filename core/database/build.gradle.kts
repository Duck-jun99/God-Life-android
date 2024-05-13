plugins {
    id("god_life.android.library")
    id("god_life.android.hilt")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.godlife.database"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.model)
    implementation(libs.androidx.junit.ktx)

}
