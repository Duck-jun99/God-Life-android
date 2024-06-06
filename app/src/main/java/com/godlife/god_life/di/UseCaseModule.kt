package com.godlife.god_life.di

import com.godlife.data.repository.LatestPostRepository
import com.godlife.data.repository.NetworkRepository
import com.godlife.data.repository.SearchPostRepository
import com.godlife.domain.CreateCommentUseCase
import com.godlife.domain.CreatePostUseCase
import com.godlife.domain.GetCommentsUseCase
import com.godlife.domain.GetLatestPostUseCase
import com.godlife.domain.GetPostDetailUseCase
import com.godlife.domain.GetUserInfoUseCase
import com.godlife.domain.SearchPostUseCase
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

    @Provides
    @Singleton
    fun provideGetLatestPostUseCase(repository: LatestPostRepository) = GetLatestPostUseCase(repository)

    @Provides
    @Singleton
    fun provideSearchPostUseCase(repository: SearchPostRepository) = SearchPostUseCase(repository)

    @Provides
    @Singleton
    fun provideGetPostDetailUseCase(repository: NetworkRepository) = GetPostDetailUseCase(repository)

    @Provides
    @Singleton
    fun provideGetCommentUseCase(repository: NetworkRepository) = GetCommentsUseCase(repository)

    @Provides
    @Singleton
    fun provideCreateCommentUseCase(repository: NetworkRepository) = CreateCommentUseCase(repository)

}