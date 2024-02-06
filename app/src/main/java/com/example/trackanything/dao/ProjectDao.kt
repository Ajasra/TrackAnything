package com.example.trackanything.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.trackanything.model.Entities.ProjNotification
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
    @Update
    suspend fun updateProject(project: Project)

    // Update a project's active status by id
    @Query("UPDATE project SET active = :active WHERE id = :id")
    suspend fun updateProjectActiveStatus(id: Int, active: Boolean)


}