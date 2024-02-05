package com.example.trackanything.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.trackanything.dao.ProjectDao
import com.example.trackanything.model.Entities.Project
import com.example.trackanything.model.Entities.Record
import android.content.Context
import androidx.room.Room

@Database(entities = [Project::class, Record::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun projectDao(): ProjectDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}