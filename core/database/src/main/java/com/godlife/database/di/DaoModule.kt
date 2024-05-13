package com.godlife.database.di

import com.godlife.database.LocalDatabase
import com.godlife.database.TodoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {
    @Provides
    fun providesTodoDao(
        database: LocalDatabase
    ): TodoDao = database.todoDao()
}