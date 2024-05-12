package com.godlife.god_life.di

import com.godlife.data.repository.NetworkRepository
import com.godlife.data.NetworkDataSourceImpl
import com.godlife.data.NetworkRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkRepositoryModule {

    @Provides
    @Singleton
    fun provideNotionRepository(
        networkDataSourceImpl: NetworkDataSourceImpl
    ): NetworkRepository {
        return NetworkRepositoryImpl(
            networkDataSourceImpl
        )
    }
}