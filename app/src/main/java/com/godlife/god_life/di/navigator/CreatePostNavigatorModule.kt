package com.godlife.god_life.di.navigator

import android.app.Activity
import android.content.Intent
import com.godlife.common.extension.startActivityWithAnimation
import com.godlife.create_post.post.CreatePostActivity
import com.godlife.navigator.CreatePostNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

internal class CreatePostNavigatorImpl @Inject constructor() : CreatePostNavigator {
    override fun navigateFrom(
        activity: Activity,
        intentBuilder: Intent.() -> Intent,
        withFinish: Boolean,
    ) {
        activity.startActivityWithAnimation<CreatePostActivity>(
            intentBuilder = intentBuilder,
            withFinish = withFinish,
        )
    }
}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class CreatePostNavigatorModule {
    @Singleton
    @Binds
    abstract fun bindCreatePostNavigator(createPostNavigatorImpl: CreatePostNavigatorImpl): CreatePostNavigator
}