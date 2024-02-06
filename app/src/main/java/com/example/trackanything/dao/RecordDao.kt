package com.example.trackanything.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.trackanything.model.Entities.Record

@Dao
interface RecordDao {
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