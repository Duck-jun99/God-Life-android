import org.jetbrains.kotlin.konan.properties.Properties

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())

plugins {
    id("god_life.android.library")
    id("god_life.android.hilt")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.godlife.network"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {


        buildConfigField(
            "String",
            "SERVER_DOMAIN",
            properties.getProperty("SERVER_DOMAIN")
        )

        buildConfigField(
            "String",
            "SERVER_IMAGE_DOMAIN",
            properties.getProperty("SERVER_IMAGE_DOMAIN")
        )

    }
}

dependencies {
    implementation(libs.androidx.junit.ktx)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.converter.gson)
    implementation(libs.androidx.tracing.ktx)

}
