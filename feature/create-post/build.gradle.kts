plugins {
    id("god_life.android.feature")
}

android {
    namespace = "com.godlife.create_post"

    buildFeatures {
        buildConfig = true
    }

}

dependencies {

    implementation(libs.androidx.junit.ktx)
    implementation(projects.core.database)
    implementation(projects.core.model)
    implementation(projects.core.network)

    implementation(projects.feature.navigator)

}