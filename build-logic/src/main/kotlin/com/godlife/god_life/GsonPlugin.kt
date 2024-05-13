package com.godlife.god_life

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

fun Project.configureGson() {


    val libs = extensions.libs
    dependencies {
        "implementation"(libs.findLibrary("gson").get())
    }
}

class GsonPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            configureGson()
        }
    }
}