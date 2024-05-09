plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidHilt") {
            id = "god_life.android.hilt"
            implementationClass = "com.godlife.god_life.HiltAndroidPlugin"
        }
        register("kotlinHilt") {
            id = "god_life.kotlin.hilt"
            implementationClass = "com.godlife.god_life.HiltKotlinPlugin"
        }
        register("androidRoom") {
            id = "god_life.android.room"
            implementationClass = "com.godlife.god_life.AndroidRoomPlugin"
        }
        register("androidGlide"){
            id = "god_life.android.glide"
            implementationClass = "com.godlife.god_life.AndroidGlidePlugin"
        }
        register("kakaoSdkUser"){
            id = "god_life.kakao.user"
            implementationClass = "com.godlife.god_life.KakaoSdkPlugin"
        }
        register("androidSecurityCrypto"){
            id = "god_life.android.security-crypto"
            implementationClass = "com.godlife.god_life.SecurityCryptoPlugin"
        }
    }
}