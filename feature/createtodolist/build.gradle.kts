plugins {
    id("god_life.android.feature")
}

android {
    namespace = "com.godlife.createtodolist"

    buildFeatures {
        buildConfig = true
    }

}

dependencies {

    implementation(libs.androidx.junit.ktx)
    implementation(projects.core.model)
}