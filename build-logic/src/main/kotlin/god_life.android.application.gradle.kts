import com.godlife.god_life.configureAdmob
import com.godlife.god_life.configureAndroidGlide
import com.godlife.god_life.configureFirebase
import com.godlife.god_life.configureHiltAndroid
import com.godlife.god_life.configureKakaoSdk
import com.godlife.god_life.configureKotestAndroid
import com.godlife.god_life.configureKotlinAndroid

plugins {
    id("com.android.application")
}

configureKotlinAndroid()
configureHiltAndroid()
configureKotestAndroid()
configureKakaoSdk()
configureFirebase()
configureAdmob()
configureAndroidGlide()