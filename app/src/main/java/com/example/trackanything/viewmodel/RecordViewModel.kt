package com.example.trackanything.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.trackanything.model.Entities.Project
import com.example.trackanything.model.Entities.Record
import com.example.trackanything.repository.RecordRepository

import kotlinx.coroutines.launch

class RecordViewModel(private val repository: RecordRepository) : ViewModel() {

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