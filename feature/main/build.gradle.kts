plugins {
    id("god_life.android.feature")
}

android {
    namespace = "com.godlife.main"
}

dependencies {

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