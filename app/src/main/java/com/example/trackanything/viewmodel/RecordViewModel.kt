package com.example.trackanything.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            repository.insert(record)
        }
    }

    // Function to delete a record
    fun deleteRecord(id: Int) {
        viewModelScope.launch {
            repository.delete(id)
        }
    }

    // Function to update a record
    fun updateRecord(record: Record) {
        viewModelScope.launch {
            repository.update(record)
        }
    }

    // Function to delete all records for a project
    fun deleteAllRecordsForProject(projectId: Int) {
        viewModelScope.launch {
            repository.deleteForProject(projectId)
        }
    }
}