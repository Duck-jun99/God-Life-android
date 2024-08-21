package com.godlife.god_life

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

fun Project.configureAdmob() {


    val libs = extensions.libs
    dependencies {
        "implementation"(libs.findLibrary("google-ads").get())
    }
}

class AdmobPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            configureAdmob()
        }
    }
}