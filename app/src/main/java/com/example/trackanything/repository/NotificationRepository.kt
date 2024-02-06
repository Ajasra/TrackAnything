package com.example.trackanything.repository

import androidx.lifecycle.LiveData
import com.example.trackanything.dao.NotificationDao
import com.example.trackanything.model.Entities.ProjNotification

class NotificationRepository(private val notificationDao: NotificationDao) {

    suspend fun insertNotification(notification: ProjNotification) {
        notificationDao.insert(notification)
    }

    fun getNotificationById(id: Int) {
        notificationDao.getNotificationById(id)
    }

    fun deleteNotification(id: Int) {
        notificationDao.deleteNotification(id)
    }

    suspend fun updateNotification(notification: ProjNotification) {
        notificationDao.updateNotification(notification)
    }

    fun getAllNotifications() {
        notificationDao.getAllNotifications()
    }

    fun getNotificationsForProject(projectId: Int): LiveData<ProjNotification> {
        return notificationDao.getNotificationsForProject(projectId)
    }

}