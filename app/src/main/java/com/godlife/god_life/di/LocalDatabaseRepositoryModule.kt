package com.godlife.god_life.di

import com.godlife.data.LocalDatabaseRepositoryImpl
import com.godlife.data.LocalPreferenceUserDataSourceImpl
import com.godlife.data.LocalPreferenceUserRepositoryImpl
import com.godlife.data.NetworkDataSourceImpl
import com.godlife.data.NetworkRepositoryImpl
import com.godlife.data.repository.LocalDatabaseRepository
import com.godlife.data.repository.LocalPreferenceUserRepository
import com.godlife.data.repository.NetworkRepository
import com.godlife.database.TodoDao
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalDatabaseRepositoryModule {

    @Provides
    @Singleton
    fun provideLocalDatabaseRepositoryModule(
        todoDao: TodoDao
    ): LocalDatabaseRepository {
        return LocalDatabaseRepositoryImpl(
            todoDao
        )
    }
}