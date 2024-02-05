package com.example.trackanything.model.Entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity
data class Project(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val description: String,
    val active: Boolean,
    val tags: String, // Consider converting to/from JSON if using multiple tags
    val valueType: String,
    // options for the value is the type 'select'
    val options: String
)

@Entity(
    foreignKeys = [ForeignKey(
        entity = Project::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("projectId"),
        onDelete = ForeignKey.CASCADE
    )],
)
data class Record(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val value: String,
    val projectId: Int,
    val timeAdded: Long,
    val note: String
)