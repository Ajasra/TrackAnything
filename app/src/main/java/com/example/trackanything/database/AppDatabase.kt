package com.example.trackanything.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.trackanything.dao.ProjectDao
import com.example.trackanything.model.Entities.Project
import com.example.trackanything.model.Entities.Record
import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.trackanything.dao.NotificationDao
import com.example.trackanything.model.Entities.ProjNotification

@Database(entities = [Project::class, Record::class, ProjNotification::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun projectDao(): ProjectDao
    abstract  fun notificationDao(): NotificationDao
    abstract fun recordDao(): com.example.trackanything.dao.RecordDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}