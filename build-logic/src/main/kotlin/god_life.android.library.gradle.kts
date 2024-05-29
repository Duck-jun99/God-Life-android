import com.godlife.god_life.configureAndroidGlide
import com.godlife.god_life.configureAndroidPaging
import com.godlife.god_life.configureAndroidRoom
import com.godlife.god_life.configureCoroutineAndroid
import com.godlife.god_life.configureGson
import com.godlife.god_life.configureHiltAndroid
import com.godlife.god_life.configureKotest
import com.godlife.god_life.configureKotlinAndroid
import com.godlife.god_life.configureLottie
import com.godlife.god_life.configureSecurityCrypto

plugins {
    id("com.android.library")
}

configureKotlinAndroid()
configureKotest()
configureCoroutineAndroid()
configureHiltAndroid()
configureAndroidGlide()
configureSecurityCrypto()
configureAndroidRoom()
configureGson()
configureAndroidPaging()

