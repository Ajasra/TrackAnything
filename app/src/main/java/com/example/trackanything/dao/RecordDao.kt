package com.example.trackanything.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.trackanything.models.Record

/**
 * This is the Data Access Object (DAO) interface for the [Record] entity.
 * It provides methods to perform database operations on [Record] objects.
 */
@Dao
interface RecordDao {

    /**
     * Retrieves all [Record]s for a specific project from the database.
     *
     * @param projectId The ID of the project.
     * @return A [LiveData] list of all [Record]s for the specified project, ordered by ID in descending order.
     */
    @Query("SELECT * FROM record WHERE projectId = :projectId ORDER BY id DESC")
    fun getByProject(projectId: Int): LiveData<List<Record>>


    /**
     * Retrieves all [Record]s for a specific project from the database.
     *
     * @param projectId The ID of the project.
     * @param time1 The start time.
     * @param time2 The end time.
     * @return A [LiveData] list of all [Record]s for the specified project, ordered by ID in ascending order.
     */
    @Query("SELECT * FROM record WHERE (projectId = :projectId) AND (timeAdded > :time1) AND (timeAdded < :time2) ORDER BY id ASC")
    fun getByProjectAndTime(projectId: Int, time1: Long, time2: Long): LiveData<List<Record>>

        /**
     * Inserts a new [Record] into the database.
     *
     * @param record The [Record] to be inserted.
     * @return The row ID of the newly inserted [Record].
     */
    @Insert
    suspend fun insert(record: Record): Long

    /**
     * Updates an existing [Record] in the database.
     *
     * @param record The [Record] to be updated.
     */
    @Update
    suspend fun update(record: Record)

    /**
     * Deletes a [Record] from the database by its ID.
     *
     * @param id The ID of the [Record] to be deleted.
     */
    @Query("DELETE FROM record WHERE id = :id")
    suspend fun delete(id: Int)

    /**
     * Deletes all [Record]s for a specific project from the database.
     *
     * @param projectId The ID of the project.
     */
    @Query("DELETE FROM record WHERE projectId = :projectId")
    suspend fun deleteForProject(projectId: Int)
}