package com.example.trackanything.repository

import com.example.trackanything.dao.NotificationDao
import com.example.trackanything.models.ProjNotification

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
    fun insert(notification: ProjNotification) {
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
     * Deletes a [ProjNotification] from the database by its project ID.
     *
     * @param projectId The ID of the project. All [ProjNotification]s associated with this project ID will be deleted.
     */
    fun deletaByProjectId(projectId: Int) {
        notificationDao.deleteByProjectId(projectId)
    }

    /**
     * Deletes all [ProjNotification]s from the database.
     */
    fun deleteAll() {
        notificationDao.deleteAll()
    }


    /**
     * Deletes all [ProjNotification]s from the database that are older than a specified time.
     *
     * @param time The time threshold. All [ProjNotification]s with a time earlier than this will be deleted.
     */
    fun deleteOlderThan(time: String) {
        notificationDao.deleteOlderThan(time)
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
     * Retrieves a [ProjNotification] from the database by its ID.
     *
     * @param id The ID of the [ProjNotification].
     * @return The [ProjNotification] object with the given ID.
     */
    fun getById(id: Int): ProjNotification {
        return notificationDao.getById(id)
    }

    /**
     * Retrieves the next [ProjNotification] from the database, ordered by time in ascending order.
     *
     * @return The [ProjNotification] object with the earliest time.
     */
    fun getNext(): ProjNotification {
        return notificationDao.getNext()
    }
}