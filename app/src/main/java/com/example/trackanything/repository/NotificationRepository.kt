package com.example.trackanything.repository

import androidx.lifecycle.LiveData
import com.example.trackanything.dao.NotificationDao
import com.example.trackanything.model.Entities.ProjNotification

/**
 * This is the repository class for the [ProjNotification] entity.
 * It provides methods to perform database operations on [ProjNotification] objects.
 * It uses the [NotificationDao] to interact with the database.
 *
 * @property notificationDao The DAO to interact with the database.
 */
class NotificationRepository(private val notificationDao: NotificationDao) {

    /**
     * Inserts a new [ProjNotification] into the database.
     *
     * @param notification The [ProjNotification] to be inserted.
     */
    suspend fun insert(notification: ProjNotification) {
        notificationDao.insert(notification)
    }

    /**
     * Deletes a [ProjNotification] from the database by its ID.
     *
     * @param id The ID of the [ProjNotification] to be deleted.
     */
    fun delete(id: Int) {
        notificationDao.deleteById(id)
    }

    /**
     * Updates an existing [ProjNotification] in the database.
     *
     * @param notification The [ProjNotification] to be updated.
     */
    suspend fun update(notification: ProjNotification) {
        notificationDao.update(notification)
    }

    /**
     * Retrieves a [ProjNotification] from the database by its project ID.
     *
     * @param projectId The ID of the project.
     * @return A [LiveData] object containing the [ProjNotification] associated with the project ID.
     */
    fun getByProjectId(projectId: Int): LiveData<ProjNotification> {
        return notificationDao.getByProjectId(projectId)
    }

}