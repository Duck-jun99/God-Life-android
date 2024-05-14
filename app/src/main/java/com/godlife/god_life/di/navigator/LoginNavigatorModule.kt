package com.godlife.god_life.di.navigator

import android.app.Activity
import android.content.Intent
import com.godlife.common.extension.startActivityWithAnimation
import com.godlife.login.LoginActivity
import com.godlife.navigator.LoginNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

internal class LoginNavigatorImpl @Inject constructor() : LoginNavigator {
    override fun navigateFrom(
        activity: Activity,
        intentBuilder: Intent.() -> Intent,
        withFinish: Boolean,
    ) {
        activity.startActivityWithAnimation<LoginActivity>(
            intentBuilder = intentBuilder,
            withFinish = withFinish,
        )
    }
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LoginNavigatorModule {
    @Singleton
    @Binds
    abstract fun bindLoginNavigator(loginNavigatorImpl: LoginNavigatorImpl): LoginNavigator
}