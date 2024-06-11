package com.godlife.god_life.di.navigator

import android.app.Activity
import android.content.Intent
import com.godlife.common.extension.startActivityWithAnimation
import com.godlife.createtodolist.CreateTodoListActivity
import com.godlife.navigator.CreatetodolistNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

internal class CreatetodolistNavigatorImpl @Inject constructor() : CreatetodolistNavigator {
    override fun navigateFrom(
        activity: Activity,
        intentBuilder: Intent.() -> Intent,
        withFinish: Boolean,
    ) {
        activity.startActivityWithAnimation<CreateTodoListActivity>(
            intentBuilder = intentBuilder,
            withFinish = withFinish,
        )
    }
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CreatetodolistNavigatorModule {
    @Singleton
    @Binds
    abstract fun bindCreatetodolistNavigator(createtodolistNavigatorImpl: CreatetodolistNavigatorImpl): CreatetodolistNavigator
}