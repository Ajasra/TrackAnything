package com.example.trackanything.repository

import androidx.lifecycle.LiveData
import com.example.trackanything.dao.ProjectDao
import com.example.trackanything.models.Project

/**
 * This is the repository class for the [Project] entity.
 * It provides methods to perform database operations on [Project] objects.
 * It uses the [ProjectDao] to interact with the database.
 *
 * @property projectDao The DAO to interact with the database.
 */
class ProjectRepository(private val projectDao: ProjectDao) {

    /**
     * Inserts a new [Project] into the database.
     *
     * @param project The [Project] to be inserted.
     * @return The row ID of the newly inserted [Project].
     */
    suspend fun insert(project: Project): Long {
        return projectDao.insert(project)
    }

    /**
     * Retrieves a [Project] from the database by its ID.
     *
     * @param id The ID of the [Project].
     * @return A [LiveData] object containing the [Project] associated with the ID.
     */
    fun getLDById(id: Int): LiveData<Project> {
        return projectDao.getLDById(id)
    }

    /**
     * Retrieves a [Project] from the database by its ID.
     *
     * @param id The ID of the [Project].
     * @return The [Project] associated with the ID.
     */
    fun getById(id: Int): Project {
        return projectDao.getById(id)
    }

    /**
     * Deletes a [Project] from the database by its ID.
     *
     * @param id The ID of the [Project] to be deleted.
     */
    suspend fun delete(id: Int) {
        projectDao.delete(id)
    }

    /**
     * Updates an existing [Project] in the database.
     *
     * @param project The [Project] to be updated.
     */
    suspend fun update(project: Project) {
        projectDao.update(project)
    }

    /**
     * Retrieves all [Project]s from the database.
     *
     * @return A [LiveData] list of all [Project]s.
     */
    fun getLDAll(): LiveData<List<Project>> {
        return projectDao.getLDAll()
    }

    /**
     * Retrieves all [Project]s from the database.
     *
     * @return A list of all [Project]s.
     */
    fun getAll(): List<Project> {
        return projectDao.getAll()
    }
}