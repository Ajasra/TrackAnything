package com.example.trackanything.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.trackanything.model.Entities.ProjNotification
import com.example.trackanything.model.Entities.Project

@Dao
interface NotificationDao {
    @Insert
    suspend fun insert(projNotification: ProjNotification)

    @Update
    suspend fun updateNotification(projNotification: ProjNotification)

    @Query("SELECT * FROM ProjNotification WHERE projectId = :projectId")
    fun getNotificationsForProject(projectId: Int): LiveData<ProjNotification>

    @Query("SELECT * FROM ProjNotification WHERE id = :id")
    fun getNotificationById(id: Int): ProjNotification

    @Query("DELETE FROM ProjNotification WHERE id = :id")
    fun deleteNotification(id: Int)

    @Query("SELECT * FROM ProjNotification")
    fun getAllNotifications(): List<ProjNotification>


}