import org.jetbrains.kotlin.konan.properties.Properties

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())


plugins {
    id("god_life.android.feature")
}

android {
    namespace = "com.godlife.main_page"

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

    implementation(projects.core.service)

    implementation(projects.feature.createtodolist)
    implementation(projects.core.database)
    implementation(projects.core.network)
    implementation(projects.feature.profile)

    implementation(projects.feature.navigator)

    implementation(libs.androidx.junit.ktx)
}