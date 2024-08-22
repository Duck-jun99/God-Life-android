plugins {
    id("god_life.android.library")
}

android {
    namespace = "com.godlife.domain"
}
dependencies {
    implementation(projects.core.data)
    implementation(projects.core.model)
    implementation(libs.google.ads)
    implementation(libs.androidx.junit.ktx)
}