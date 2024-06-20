package com.godlife.god_life.di

import com.godlife.data.repository.LatestPostRepository
import com.godlife.data.repository.NetworkRepository
import com.godlife.data.repository.SearchPostRepository
import com.godlife.domain.CheckUserExistenceUseCase
import com.godlife.domain.CreateCommentUseCase
import com.godlife.domain.CreatePostUseCase
import com.godlife.domain.DeleteCommentUseCase
import com.godlife.domain.GetCommentsUseCase
import com.godlife.domain.GetLatestPostUseCase
import com.godlife.domain.GetPostDetailUseCase
import com.godlife.domain.GetRankingUseCase
import com.godlife.domain.GetUserInfoUseCase
import com.godlife.domain.GetUserProfileUseCase
import com.godlife.domain.GetWeeklyFamousPostUseCase
import com.godlife.domain.PlusGodScoreUseCase
import com.godlife.domain.PostNotificationTimeUseCase
import com.godlife.domain.ReissueUseCase
import com.godlife.domain.SearchPostUseCase
import com.godlife.domain.SignUpUseCase
import com.godlife.domain.UpdateUserInfoUseCase
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
    fun provideGetUserInfoUseCase(repository: NetworkRepository) = GetUserInfoUseCase(repository)

    @Provides
    @Singleton
    fun provideGetUserProfileUseCase(repository: NetworkRepository) = GetUserProfileUseCase(repository)

    @Provides
    @Singleton
    fun provideReissueUseCase(repository: NetworkRepository) = ReissueUseCase(repository)

    @Provides
    @Singleton
    fun provideCheckUserExistUseCase(repository: NetworkRepository) = CheckUserExistenceUseCase(repository)

    @Provides
    @Singleton
    fun provideSignUpUseCase(repository: NetworkRepository) = SignUpUseCase(repository)

    @Provides
    @Singleton
    fun provideUpdateUserInfoUseCase(repository: NetworkRepository) = UpdateUserInfoUseCase(repository)

    @Provides
    @Singleton
    fun provideCreatePostUseCase(repository: NetworkRepository) = CreatePostUseCase(repository)

    @Provides
    @Singleton
    fun provideGetLatestPostUseCase(repository: LatestPostRepository) = GetLatestPostUseCase(repository)

    @Provides
    @Singleton
    fun provideGetWeeklyFamousPostUseCase(repository: NetworkRepository) = GetWeeklyFamousPostUseCase(repository)

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

    @Provides
    @Singleton
    fun provideDeleteCommentUseCase(repository: NetworkRepository) = DeleteCommentUseCase(repository)

    @Provides
    @Singleton
    fun providePlusGodScoreUseCase(repository: NetworkRepository) = PlusGodScoreUseCase(repository)

    @Provides
    @Singleton
    fun provideGetRankingUseCase(repository: NetworkRepository) = GetRankingUseCase(repository)

    @Provides
    @Singleton
    fun providePostNotificationTimeUseCase(repository: NetworkRepository) = PostNotificationTimeUseCase(repository)
}