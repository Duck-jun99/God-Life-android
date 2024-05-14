package com.godlife.god_life

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

fun Project.configureLottie() {


    val libs = extensions.libs
    dependencies {
        "implementation"(libs.findLibrary("lottie").get())
    }
}

class LottiePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            configureLottie()
        }
    }
}