plugins {
    id("god_life.android.feature")
}

android {
    namespace = "com.godlife.createpost"

    buildFeatures {
        buildConfig = true
    }

}

dependencies {

    implementation(libs.androidx.junit.ktx)
    implementation(projects.core.database)
    implementation(projects.core.model)

    implementation(projects.feature.navigator)

}