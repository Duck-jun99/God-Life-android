package com.godlife.god_life

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

fun Project.configureSandwich() {


    val libs = extensions.libs
    dependencies {
        "implementation"(libs.findLibrary("sandwich").get())
    }
}

class SandwichPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            configureSandwich()
        }
    }
}