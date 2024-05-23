plugins {
    id("god_life.android.library")
    id("god_life.android.hilt")
    id("god_life.kotlin.hilt")
}

android {
    namespace = "com.godlife.common"
}
dependencies {
    implementation(libs.androidx.junit.ktx)
}