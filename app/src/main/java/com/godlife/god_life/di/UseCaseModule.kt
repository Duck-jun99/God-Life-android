package com.godlife.god_life.di

import com.godlife.data.repository.NetworkRepository
import com.godlife.domain.CreatePostUseCase
import com.godlife.domain.GetUserInfoUseCase
import com.godlife.domain.SignUpUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    @Singleton
    fun provideGetUserRepoUseCase(repository: NetworkRepository) = GetUserInfoUseCase(repository)

    @Provides
    @Singleton
    fun provideSignUpUseCase(repository: NetworkRepository) = SignUpUseCase(repository)

    @Provides
    @Singleton
    fun provideCreatePostUseCase(repository: NetworkRepository) = CreatePostUseCase(repository)
}