package com.example.trackanything.repository

import androidx.lifecycle.LiveData
import com.example.trackanything.dao.RecordDao
import com.example.trackanything.model.Entities.Record


class RecordRepository (private val recordDao:RecordDao) {
    fun getRecordsForProject(projectId: Int): LiveData<List<Record>> {
        return recordDao.getRecordsForProject(projectId)
    }

    suspend fun insertRecord(record: Record): Long {
        return recordDao.insertRecord(record)
    }

    suspend fun deleteRecord(id: Int) {
        recordDao.deleteRecord(id)
    }

    suspend fun updateRecord(id: Int, value: String) {
        recordDao.updateRecord(id, value)
    }

    suspend fun deleteAllRecordsForProject(projectId: Int) {
        recordDao.deleteAllRecordsForProject(projectId)
    }
}
