package com.example.trackanything.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.trackanything.dao.ProjectDao
import com.example.trackanything.model.Entities.Project
import com.example.trackanything.model.Entities.Record
import android.content.Context
import androidx.room.Room
import com.example.trackanything.dao.NotificationDao
import com.example.trackanything.model.Entities.ProjNotification

/**
 * This is the main database class for the application.
 * It uses Room persistence library to abstract the SQLite database.
 *
 * @property ProjectDao provides access to [Project] related operations.
 * @property NotificationDao provides access to [ProjNotification] related operations.
 * @property RecordDao provides access to [Record] related operations.
 */
@Database(entities = [Project::class, Record::class, ProjNotification::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    /**
     * Abstract method to get the instance of [ProjectDao].
     *
     * @return instance of [ProjectDao].
     */
    abstract fun projectDao(): ProjectDao

    /**
     * Abstract method to get the instance of [NotificationDao].
     *
     * @return instance of [NotificationDao].
     */
    abstract fun notificationDao(): NotificationDao

    /**
     * Abstract method to get the instance of [RecordDao].
     *
     * @return instance of [RecordDao].
     */
    abstract fun recordDao(): com.example.trackanything.dao.RecordDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Singleton pattern to get the instance of [AppDatabase].
         *
         * @param context application context.
         * @return instance of [AppDatabase].
         */
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