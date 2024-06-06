import org.jetbrains.kotlin.konan.properties.Properties

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())

plugins {
    id("god_life.android.feature")
}

android {
    namespace = "com.godlife.community_page"

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

    implementation(projects.core.model)
    implementation(projects.core.network)
    implementation(projects.core.designsystem)
    implementation(libs.androidx.junit.ktx)
}