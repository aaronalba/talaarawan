package com.aaron.talaarawan.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class, Entry::class], version = 1)
abstract class JournalRoomDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun entryDao(): EntryDao

    companion object {
        @Volatile
        var INSTANCE: JournalRoomDatabase? = null

        fun getDatabase(context: Context): JournalRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    JournalRoomDatabase::class.java,
                    "app_database"
                )
                    // destroys the database upon migration, replace this when updating the database schema
                    .fallbackToDestructiveMigration()
                    .build()
                return instance
            }
        }
    }
}