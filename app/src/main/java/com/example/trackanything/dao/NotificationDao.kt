package com.example.trackanything.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.trackanything.model.Entities.ProjNotification

/**
 * This is the Data Access Object (DAO) interface for the [ProjNotification] entity.
 * It provides methods to perform database operations on [ProjNotification] objects.
 */
@Dao
interface NotificationDao {

    /**
     * Inserts a new [ProjNotification] into the database.
     *
     * @param projNotification The [ProjNotification] to be inserted.
     */
    @Insert
    suspend fun insert(projNotification: ProjNotification)

    /**
     * Updates an existing [ProjNotification] in the database.
     *
     * @param projNotification The [ProjNotification] to be updated.
     */
    @Update
    suspend fun update(projNotification: ProjNotification)

    /**
     * Retrieves a [ProjNotification] from the database by its project ID.
     *
     * @param projectId The ID of the project.
     * @return A [LiveData] object containing the [ProjNotification] associated with the project ID.
     */
    @Query("SELECT * FROM ProjNotification WHERE projectId = :projectId")
    fun getByProjectId(projectId: Int): LiveData<ProjNotification>

    /**
     * Deletes a [ProjNotification] from the database by its ID.
     *
     * @param id The ID of the [ProjNotification] to be deleted.
     */
    @Query("DELETE FROM ProjNotification WHERE id = :id")
    fun deleteById(id: Int)
}