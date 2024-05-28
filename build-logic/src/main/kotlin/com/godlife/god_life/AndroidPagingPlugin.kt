package com.godlife.god_life

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

fun Project.configureAndroidPaging() {
    with(pluginManager) {
        apply("org.jetbrains.kotlin.kapt")
    }

    val libs = extensions.libs
    dependencies {
        "implementation"(libs.findLibrary("androidx-paging-common").get())
        "implementation"(libs.findLibrary("androidx-paging-runtime").get())
        "implementation"(libs.findLibrary("androidx-paging-compose").get())
    }
}

class AndroidPagingPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            configureAndroidPaging()
        }
    }
}