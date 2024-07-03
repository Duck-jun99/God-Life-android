package com.godlife.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.godlife.database.model.TodoEntity
import com.godlife.database.util.ListConverter
import com.godlife.database.util.NotificationTimeDataConverter
import com.godlife.database.util.TodoTimeDataConverter

@Database(
    entities = [TodoEntity::class],
    version = 2
)
@TypeConverters(
    ListConverter::class,
    TodoTimeDataConverter::class,
    NotificationTimeDataConverter::class
)
abstract class LocalDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDao

    /*
    companion object {
        @Volatile
        private var database: RoomDatabase? = null

        fun getDatabase(context: Context): RoomDatabase {
            return database ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomDatabase::class.java,
                    "app_database"
                ).build()
                database = instance
                instance
            }
        }
    }

     */
}
