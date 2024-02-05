package com.example.trackanything.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackanything.model.Entities.Project
import com.example.trackanything.model.Entities.Record
import com.example.trackanything.repository.ProjectRepository
import kotlinx.coroutines.launch

class ProjectViewModel(private val repository: ProjectRepository) : ViewModel() {
    // LiveData to observe projects
    val allProjects: LiveData<List<Project>> = repository.getAllProjects()

    // Function to add a project
    fun addProject(project: Project) {
        viewModelScope.launch {
            repository.insertProject(project)
        }
    }

    // Function to get a project by id
    fun getProjectById(id: Int): LiveData<Project> {
        return repository.getProjectById(id)
    }

    // Function to delete a project
    fun deleteProject(id: Int) {
        viewModelScope.launch {
            repository.deleteProject(id)
        }
    }

    // Function to update a project
    fun updateProject(id: Int, name: String, description: String, active: Boolean, tags: String, valueType: String) {
        viewModelScope.launch {
            repository.updateProject(id, name, description, active, tags, valueType)
        }
    }

    // Function to update a project's active status
    fun updateProjectActiveStatus(id: Int, active: Boolean) {
        viewModelScope.launch {
            repository.updateProjectActiveStatus(id, active)
        }
    }

    // Function to get records for a project
    fun getRecordsForProject(projectId: Int): LiveData<List<Record>> {
        return repository.getRecordsForProject(projectId)
    }

    // Function to insert a record
    fun insertRecord(record: Record) {
        viewModelScope.launch {
            repository.insertRecord(record)
        }
    }

    // Function to delete a record
    fun deleteRecord(id: Int) {
        viewModelScope.launch {
            repository.deleteRecord(id)
        }
    }

    // Function to update a record
    fun updateRecord(id: Int, value: String) {
        viewModelScope.launch {
            repository.updateRecord(id, value)
        }
    }

    // Function to delete all records for a project
    fun deleteAllRecordsForProject(projectId: Int) {
        viewModelScope.launch {
            repository.deleteAllRecordsForProject(projectId)
        }
    }
}