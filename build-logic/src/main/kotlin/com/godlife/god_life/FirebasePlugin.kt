package com.godlife.god_life

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

fun Project.configureFirebase() {
    

    val libs = extensions.libs
    dependencies {
        "implementation"(libs.findLibrary("firebase-bom").get())
        "implementation"(libs.findLibrary("firebase-messaging").get())
        "implementation"(libs.findLibrary("firebase-analytics").get())
    }
}

class FirebasePlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            configureFirebase()
        }
    }
}