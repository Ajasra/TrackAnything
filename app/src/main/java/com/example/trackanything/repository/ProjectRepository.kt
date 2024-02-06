package com.example.trackanything.repository

import androidx.lifecycle.LiveData
import com.example.trackanything.dao.ProjectDao
import com.example.trackanything.model.Entities.Project
import com.example.trackanything.model.Entities.Record

class ProjectRepository(private val projectDao: ProjectDao) {

    suspend fun insertProject(project: Project): Long {
        return projectDao.insertProject(project)
    }

    fun getProjectById(id: Int): LiveData<Project> {
        return projectDao.getProjectById(id)
    }

    suspend fun deleteProject(id: Int) {
        projectDao.deleteProject(id)
    }

    suspend fun updateProject( project: Project) {
        projectDao.updateProject(project)
    }

    suspend fun updateProjectActiveStatus(id: Int, active: Boolean) {
        projectDao.updateProjectActiveStatus(id, active)
    }

    fun getAllProjects(): LiveData<List<Project>> {
        return projectDao.getAllProjects()
    }
}