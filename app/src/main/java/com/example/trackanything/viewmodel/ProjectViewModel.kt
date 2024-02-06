package com.example.trackanything.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackanything.model.Entities.Project
import com.example.trackanything.repository.ProjectRepository
import kotlinx.coroutines.launch

class ProjectViewModel(private val repository: ProjectRepository) : ViewModel() {
    // LiveData to observe projects
    val allProjects: LiveData<List<Project>> = repository.getAll()

    // Function to add a project
    fun addProject(project: Project) {
        viewModelScope.launch {
            repository.insert(project)
        }
    }

    // Function to get a project by id
    fun getProjectById(id: Int): LiveData<Project> {
        return repository.getById(id)
    }

    // Function to delete a project
    fun deleteProject(id: Int) {
        viewModelScope.launch {
            repository.delete(id)
        }
    }

    // Function to update a project
    fun updateProject(project: Project) {
        viewModelScope.launch {
            repository.update(project)
        }
    }


}