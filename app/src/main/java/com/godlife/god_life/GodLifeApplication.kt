package com.godlife.god_life

import android.app.Application
import com.godlife.main.BuildConfig
import com.google.android.gms.ads.MobileAds
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class GodLifeApplication: Application(){
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)

        MobileAds.initialize(this)
    }
}