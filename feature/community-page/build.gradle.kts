plugins {
    id("god_life.android.feature")
}

android {
    namespace = "com.godlife.community_page"

    buildFeatures {
        buildConfig = true
    }


}

dependencies {

    implementation(projects.core.model)
    implementation(projects.core.network)
    implementation(projects.core.designsystem)
    implementation(libs.androidx.junit.ktx)
}