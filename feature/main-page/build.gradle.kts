plugins {
    id("god_life.android.feature")
}

android {
    namespace = "com.godlife.main_page"

    buildFeatures {
        buildConfig = true
    }


}

dependencies {

    implementation(projects.feature.createtodolist)

    implementation(libs.androidx.junit.ktx)
}