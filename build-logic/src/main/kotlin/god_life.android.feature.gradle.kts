import com.godlife.god_life.configureAndroidPaging
import com.godlife.god_life.configureFirebase
import com.godlife.god_life.configureHiltAndroid
import com.godlife.god_life.configureKakaoSdk
import com.godlife.god_life.configureLottie
import com.godlife.god_life.libs

plugins {
    id("god_life.android.library")
    id("god_life.android.compose")
}

android {
    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}

configureHiltAndroid()
configureKakaoSdk()
configureLottie()
configureAndroidPaging()
configureFirebase()

dependencies {
    implementation(project(":core:model"))
    implementation(project(":core:designsystem"))
    implementation(project(":core:domain"))


    val libs = project.extensions.libs
    implementation(libs.findLibrary("hilt.navigation.compose").get())
    implementation(libs.findLibrary("androidx.compose.navigation").get())
    androidTestImplementation(libs.findLibrary("androidx.compose.navigation.test").get())

    implementation(libs.findLibrary("androidx.lifecycle.viewModelCompose").get())
    implementation(libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
}
