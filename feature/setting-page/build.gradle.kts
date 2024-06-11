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

    implementation(projects.feature.navigator)
    implementation(projects.feature.profile)

    implementation(projects.core.model)
    implementation(projects.core.designsystem)
    implementation(projects.core.network)

    implementation(libs.androidx.junit.ktx)
}