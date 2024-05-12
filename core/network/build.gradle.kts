plugins {
    id("god_life.android.library")
    id("god_life.android.hilt")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.godlife.network"

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(libs.androidx.junit.ktx)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.converter.gson)
    implementation(libs.androidx.tracing.ktx)

}
