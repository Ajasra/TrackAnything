package com.example.trackanything.repository

import androidx.lifecycle.LiveData
import com.example.trackanything.dao.ProjectDao
import com.example.trackanything.model.Entities.Project
import com.example.trackanything.model.Entities.Record

class ProjectRepository(private val projectDao: ProjectDao) {

    suspend fun insertProject(project: Project) {
        projectDao.insertProject(project)
    }

    fun getProjectById(id: Int): LiveData<Project> {
        return projectDao.getProjectById(id)
    }

    suspend fun deleteProject(id: Int) {
        projectDao.deleteProject(id)
    }

    suspend fun updateProject(id: Int, name: String, description: String, active: Boolean, tags: String, valueType: String) {
        projectDao.updateProject(id, name, description, active, tags, valueType)
    }

    suspend fun updateProjectActiveStatus(id: Int, active: Boolean) {
        projectDao.updateProjectActiveStatus(id, active)
    }

    fun getRecordsForProject(projectId: Int): LiveData<List<Record>> {
        return projectDao.getRecordsForProject(projectId)
    }

    suspend fun insertRecord(record: Record): Long {
        return projectDao.insertRecord(record)
    }

    suspend fun deleteRecord(id: Int) {
        projectDao.deleteRecord(id)
    }

    suspend fun updateRecord(id: Int, value: String) {
        projectDao.updateRecord(id, value)
    }

    suspend fun deleteAllRecordsForProject(projectId: Int) {
        projectDao.deleteAllRecordsForProject(projectId)
    }

    fun getAllProjects(): LiveData<List<Project>> {
        return projectDao.getAllProjects()
    }
}