package com.godlife.god_life

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

fun Project.configureSecurityCrypto() {


    val libs = extensions.libs
    dependencies {
        "implementation"(libs.findLibrary("androidx-security-crypto").get())
    }
}

class SecurityCryptoPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            configureSecurityCrypto()
        }
    }
}