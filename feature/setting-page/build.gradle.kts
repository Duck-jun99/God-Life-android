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

    implementation(projects.feature.main)

    implementation(projects.core.data)
    implementation(projects.core.designsystem)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.kotlinx.immutable)

    implementation(libs.androidx.junit.ktx)
}