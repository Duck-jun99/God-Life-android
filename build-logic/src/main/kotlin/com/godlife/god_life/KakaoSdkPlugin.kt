package com.godlife.god_life

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

fun Project.configureKakaoSdk() {
    

    val libs = extensions.libs
    dependencies {
        "implementation"(libs.findLibrary("kakao-sdk-v2-user").get())
    }
}

class KakaoSdkPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            configureKakaoSdk()
        }
    }
}