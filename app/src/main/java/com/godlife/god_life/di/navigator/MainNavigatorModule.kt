package com.godlife.god_life.di.navigator

import android.app.Activity
import android.content.Intent
import com.godlife.common.extension.startActivityWithAnimation
import com.godlife.main.MainActivity
import com.godlife.navigator.MainNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

internal class MainNavigatorImpl @Inject constructor() : MainNavigator {
    override fun navigateFrom(
        activity: Activity,
        intentBuilder: Intent.() -> Intent,
        withFinish: Boolean,
    ) {
        activity.startActivityWithAnimation<MainActivity>(
            intentBuilder = intentBuilder,
            withFinish = withFinish,
        )
    }
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class MainNavigatorModule {
    @Singleton
    @Binds
    abstract fun bindMainNavigator(mainNavigatorImpl: MainNavigatorImpl): MainNavigator
}