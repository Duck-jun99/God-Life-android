package com.godlife.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.godlife.database.LocalDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun providesLocalDatabase(
        @ApplicationContext context: Context,
    ): LocalDatabase = Room.databaseBuilder(
        context,
        LocalDatabase::class.java,
        "app-database",
    ).build()
}
