package com.example.trackanything.repository

import androidx.lifecycle.LiveData
import com.example.trackanything.dao.RecordDao
import com.example.trackanything.models.Record

/**
 * This is the repository class for the [Record] entity.
 * It provides methods to perform database operations on [Record] objects.
 * It uses the [RecordDao] to interact with the database.
 *
 * @property recordDao The DAO to interact with the database.
 */
class RecordRepository(private val recordDao: RecordDao) {

    /**
     * Retrieves all [Record]s for a specific project from the database.
     *
     * @param projectId The ID of the project.
     * @return A [LiveData] list of all [Record]s for the specified project, ordered by ID in descending order.
     */
    fun getRecordsForProject(projectId: Int): LiveData<List<Record>> {
        return recordDao.getByProject(projectId)
    }

    /**
     * Inserts a new [Record] into the database.
     *
     * @param record The [Record] to be inserted.
     * @return The row ID of the newly inserted [Record].
     */
    suspend fun insert(record: Record): Long {
        return recordDao.insert(record)
    }

    /**
     * Deletes a [Record] from the database by its ID.
     *
     * @param id The ID of the [Record] to be deleted.
     */
    suspend fun delete(id: Int) {
        recordDao.delete(id)
    }

    /**
     * Updates an existing [Record] in the database.
     *
     * @param record The [Record] to be updated.
     */
    suspend fun update(record: Record) {
        recordDao.update(record)
    }

    /**
     * Deletes all [Record]s for a specific project from the database.
     *
     * @param projectId The ID of the project.
     */
    suspend fun deleteForProject(projectId: Int) {
        recordDao.deleteForProject(projectId)
    }
}