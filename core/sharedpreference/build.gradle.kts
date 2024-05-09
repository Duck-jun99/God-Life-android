plugins {
    id("god_life.android.library")
    id("god_life.android.hilt")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.godlife.sharedpreference"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.junit.ktx)

}
