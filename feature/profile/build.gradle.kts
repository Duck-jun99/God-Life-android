plugins {
    id("god_life.android.feature")
    id("god_life.android.library")
}

android {
    namespace = "com.godlife.profile"

    buildFeatures {
        buildConfig = true
    }


}

dependencies {

    implementation(libs.androidx.junit.ktx)

    implementation(projects.feature.navigator)

}