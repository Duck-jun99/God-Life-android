import org.jetbrains.kotlin.konan.properties.Properties

val properties = Properties()
properties.load(project.rootProject.file("local.properties").inputStream())

plugins {
    id("god_life.android.application")
    id("god_life.android.security-crypto")
    id("org.jetbrains.kotlin.android")

    id("god_life.android.hilt")
    id("god_life.android.room")
}

android {
    namespace = "com.godlife.god_life"

    defaultConfig {
        applicationId = "com.godlife.god_life"
        versionCode = 1
        versionName = "1.0"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    
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

    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.designsystem)
    implementation(projects.core.network)
    implementation(projects.core.database)
    implementation(projects.core.common)

    implementation(projects.feature.main)
    implementation(projects.feature.login)
    implementation(projects.feature.createtodolist)
    implementation(projects.feature.navigator)

    implementation(libs.kotlinx.serialization.json)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.converter.gson)

    implementation(libs.androidx.activity.compose)

    implementation(libs.androidx.core.ktx)

    implementation(libs.androidx.lifecycle.runtimeCompose)

    implementation(libs.androidx.tracing.ktx)
    implementation(libs.androidx.foundation.layout.android)



    debugImplementation(libs.androidx.compose.ui.testManifest)


    testImplementation(libs.androidx.compose.ui.test)
    testImplementation(libs.hilt.android.testing)

    androidTestImplementation(libs.androidx.test.espresso.core)

    androidTestImplementation(libs.androidx.compose.ui.test)
    androidTestImplementation(libs.hilt.android.testing)

    

}

