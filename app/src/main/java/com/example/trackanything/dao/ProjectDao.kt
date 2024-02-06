package com.example.trackanything.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.trackanything.model.Entities.Project

/**
 * This is the Data Access Object (DAO) interface for the [Project] entity.
 * It provides methods to perform database operations on [Project] objects.
 */
@Dao
interface ProjectDao {

    /**
     * Retrieves all [Project]s from the database.
     *
     * @return A [LiveData] list of all [Project]s.
     */
    @Query("SELECT * FROM project")
    fun getAll(): LiveData<List<Project>>

    /**
     * Inserts a new [Project] into the database.
     *
     * @param project The [Project] to be inserted.
     * @return The row ID of the newly inserted [Project].
     */
    @Insert
    suspend fun insert(project: Project): Long

    /**
     * Retrieves a [Project] from the database by its ID.
     *
     * @param id The ID of the [Project].
     * @return A [LiveData] object containing the [Project] associated with the ID.
     */
    @Query("SELECT * FROM project WHERE id = :id")
    fun getById(id: Int): LiveData<Project>

    /**
     * Deletes a [Project] from the database by its ID.
     *
     * @param id The ID of the [Project] to be deleted.
     */
    @Query("DELETE FROM project WHERE id = :id")
    suspend fun delete(id: Int)

    /**
     * Updates an existing [Project] in the database.
     *
     * @param project The [Project] to be updated.
     */
    @Update
    suspend fun update(project: Project)
}