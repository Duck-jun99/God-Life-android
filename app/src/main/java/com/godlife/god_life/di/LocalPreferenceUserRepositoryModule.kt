package com.godlife.god_life.di

import com.godlife.data.LocalPreferenceUserDataSourceImpl
import com.godlife.data.LocalPreferenceUserRepositoryImpl
import com.godlife.data.repository.LocalPreferenceUserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalPreferenceUserRepositoryModule {

    @Provides
    @Singleton
    fun provideLocalPreferenceUserRepository(
     localPreferenceUserDataSourceImpl: LocalPreferenceUserDataSourceImpl
    ): LocalPreferenceUserRepository{
        return LocalPreferenceUserRepositoryImpl(
            localPreferenceUserDataSourceImpl
        )
    }
}