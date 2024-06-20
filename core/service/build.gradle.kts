plugins {
    id("god_life.android.library")
    id("god_life.android.hilt")
    id("org.jetbrains.kotlin.android")

    id("com.google.gms.google-services")
    id("god_life.firebase")
}

android {
    namespace = "com.godlife.service"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.junit.ktx)

    implementation(projects.feature.navigator)

}
