plugins {
    id("god_life.android.application")
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
}

dependencies {

    implementation(projects.core.data)
    implementation(projects.core.domain)
    implementation(projects.core.designsystem)

    implementation(projects.feature.main)

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

