package com.godlife.god_life.di

import com.godlife.data.repository.LatestPostRepository
import com.godlife.data.repository.LatestStimulusPostRepository
import com.godlife.data.repository.NetworkRepository
import com.godlife.data.repository.SearchPostRepository
import com.godlife.domain.CheckUserExistenceUseCase
import com.godlife.domain.CreateCommentUseCase
import com.godlife.domain.CreatePostUseCase
import com.godlife.domain.CreateStimulusPostUseCase
import com.godlife.domain.DeleteCommentUseCase
import com.godlife.domain.DeletePostUseCase
import com.godlife.domain.GetCommentsUseCase
import com.godlife.domain.GetFamousAuthorStimulusPostUseCase
import com.godlife.domain.GetLatestPostUseCase
import com.godlife.domain.GetLatestStimulusPostUseCase
import com.godlife.domain.GetPostDetailUseCase
import com.godlife.domain.GetRankingUseCase
import com.godlife.domain.GetUserInfoUseCase
import com.godlife.domain.GetUserProfileUseCase
import com.godlife.domain.GetFamousPostUseCase
import com.godlife.domain.GetFamousStimulusPostUseCase
import com.godlife.domain.GetMostViewStimulusPostUseCase
import com.godlife.domain.GetRecommendedStimulusPostUseCase
import com.godlife.domain.PlusGodScoreUseCase
import com.godlife.domain.PostNotificationTimeUseCase
import com.godlife.domain.ReissueUseCase
import com.godlife.domain.SearchPostUseCase
import com.godlife.domain.SignUpUseCase
import com.godlife.domain.UpdatePostUseCase
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
    fun provideUpdatePostUseCase(repository: NetworkRepository) = UpdatePostUseCase(repository)

    @Provides
    @Singleton
    fun provideDeletePostUseCase(repository: NetworkRepository) = DeletePostUseCase(repository)

    @Provides
    @Singleton
    fun provideGetLatestPostUseCase(repository: LatestPostRepository) = GetLatestPostUseCase(repository)

    @Provides
    @Singleton
    fun provideGetWeeklyFamousPostUseCase(repository: NetworkRepository) = GetFamousPostUseCase(repository)

    @Provides
    @Singleton
    fun provideSearchPostUseCase(searchPostRepository: SearchPostRepository, networkRepository: NetworkRepository) = SearchPostUseCase(searchPostRepository, networkRepository)

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

    @Provides
    @Singleton
    fun provideCreateStimulusPostUseCase(repository: NetworkRepository) = CreateStimulusPostUseCase(repository)

    @Provides
    @Singleton
    fun provideGetLatestStimulusPostUseCase(repository: LatestStimulusPostRepository) = GetLatestStimulusPostUseCase(repository)

    @Provides
    @Singleton
    fun provideGetFamousStimulusPostUseCase(repository: NetworkRepository) = GetFamousStimulusPostUseCase(repository)

    @Provides
    @Singleton
    fun provideGetMostViewStimulusPostUseCase(repository: NetworkRepository) = GetMostViewStimulusPostUseCase(repository)

    @Provides
    @Singleton
    fun provideGetFamousAuthorStimulusPostUseCase(repository: NetworkRepository) = GetFamousAuthorStimulusPostUseCase(repository)

    @Provides
    @Singleton
    fun provideGetRecommendedStimulusPostUseCase(repository: NetworkRepository) = GetRecommendedStimulusPostUseCase(repository)
}