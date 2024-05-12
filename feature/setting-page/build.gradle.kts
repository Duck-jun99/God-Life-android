plugins {
    id("god_life.android.feature")
}

android {
    namespace = "com.godlife.setting_page"

    buildFeatures {
        buildConfig = true
    }


}

dependencies {

    implementation(libs.androidx.junit.ktx)
}