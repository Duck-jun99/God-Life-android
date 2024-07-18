package com.godlife.god_life


import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

fun Project.configureAndroidGlide() {
    with(pluginManager) {
        apply("org.jetbrains.kotlin.kapt")
    }

    val libs = extensions.libs
    dependencies {
        "implementation"(libs.findLibrary("bumptech-glide").get())
        "implementation"(libs.findLibrary("landscapist-glide").get())
        "kapt"(libs.findLibrary("bumptech-glide-compiler").get())
    }
}

class AndroidGlidePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            configureAndroidGlide()
        }
    }
}