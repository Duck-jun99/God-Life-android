import org.jetbrains.kotlin.konan.properties.Properties

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())

plugins {
    id("god_life.android.feature")
    id("god_life.android.library")
}

android {
    namespace = "com.godlife.profile"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField(
            "String",
            "SERVER_IMAGE_DOMAIN",
            properties.getProperty("SERVER_IMAGE_DOMAIN")
        )

    }


}

dependencies {

    implementation(libs.androidx.junit.ktx)

    implementation(projects.feature.navigator)

    implementation(projects.core.network)

}