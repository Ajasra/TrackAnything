package com.example.trackanything.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.trackanything.model.Entities.Project
import com.example.trackanything.model.Entities.Record

@Dao
interface ProjectDao {
    // LiveData list of all projects. Observed by ViewModel to update UI accordingly.

    //PROJECTS
    // Get all projects
    @Query("SELECT * FROM project")
    fun getAllProjects(): LiveData<List<Project>>

    // Insert a project
    @Insert
    suspend fun insertProject(project: Project): Long

    // Get a project by id
    @Query("SELECT * FROM project WHERE id = :id")
    fun getProjectById(id: Int): LiveData<Project>

    // Delete a project by id
    @Query("DELETE FROM project WHERE id = :id")
    suspend fun deleteProject(id: Int)

    // Update a project by id
    @Query("UPDATE project SET name = :name, description = :description, active = :active, tags = :tags, valueType = :valueType WHERE id = :id")
    suspend fun updateProject(id: Int, name: String, description: String, active: Boolean, tags: String, valueType: String)

    // Update a project's active status by id
    @Query("UPDATE project SET active = :active WHERE id = :id")
    suspend fun updateProjectActiveStatus(id: Int, active: Boolean)

    //RECORDS
    // Get all records for a project
    @Query("SELECT * FROM record WHERE projectId = :projectId ORDER BY id DESC")
    fun getRecordsForProject(projectId: Int): LiveData<List<Record>>

    // Insert a record
    @Insert()
    suspend fun insertRecord(record: Record): Long

    // Delete a record by id
    @Query("DELETE FROM record WHERE id = :id")
    suspend fun deleteRecord(id: Int)

    // Update a record by id
    @Query("UPDATE record SET value = :value WHERE id = :id")
    suspend fun updateRecord(id: Int, value: String)

    // Delete all records for a project
    @Query("DELETE FROM record WHERE projectId = :projectId")
    suspend fun deleteAllRecordsForProject(projectId: Int)

}