plugins {
    id("god_life.android.library")
    id("god_life.android.hilt")
    id("god_life.kotlin.hilt")
}

android {
    namespace = "com.godlife.data"
}
dependencies {

    //api(projects.core.network)
    //api(projects.core.database)

    implementation(libs.androidx.junit.ktx)

    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.converter.gson)
}