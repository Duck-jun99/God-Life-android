import org.jetbrains.kotlin.konan.properties.Properties

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())

plugins {
    id("god_life.android.feature")
}

android {
    namespace = "com.godlife.main"

    buildFeatures {
        buildConfig = true
    }

    defaultConfig {
        buildConfigField(
            "String",
            "KAKAO_NATIVE_APP_KEY",
            properties.getProperty("KAKAO_NATIVE_APP_KEY")
        )


        manifestPlaceholders["NATIVE_APP_KEY"] =
            properties.getProperty("KAKAO_NATIVE_APP_KEY_NO_QUOTES")
    }
}

dependencies {

    //implementation(projects.core.data)
    //implementation(projects.core.designsystem)

    implementation(projects.core.service)

    implementation(projects.feature.mainPage)
    implementation(projects.feature.communityPage)
    implementation(projects.feature.settingPage)
    implementation(projects.feature.profile)

    implementation(projects.feature.navigator)
    
    //implementation(projects.feature.createtodolist)



    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtimeCompose)
    implementation(libs.androidx.lifecycle.viewModelCompose)
    implementation(libs.kotlinx.immutable)

    implementation(libs.androidx.junit.ktx)
}