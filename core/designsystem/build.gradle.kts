plugins {
    id("god_life.android.library")
    id("god_life.android.compose")
}

android {
    namespace = "com.godlife.designsystem"
}

dependencies {
    implementation(libs.androidx.appcompat)

    implementation(libs.androidx.junit.ktx)
}
