package com.example.trackanything.model.Entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

/**
 * Represents a Project entity in the database.
 *
 * @property id The unique ID of the project.
 * @property name The name of the project.
 * @property description A description of the project.
 * @property active A boolean indicating whether the project is active.
 * @property tags A string representing tags associated with the project.
 * @property valueType A string representing the type of value the project holds.
 * @property options A string representing options associated with the project.
 */
@Entity
data class Project(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val description: String,
    val active: Boolean,
    val tags: String,
    val valueType: String,
    val options: String
)

/**
 * Represents a Record entity in the database.
 *
 * @property id The unique ID of the record.
 * @property value The value of the record.
 * @property projectId The ID of the project that this record is associated with.
 * @property timeAdded The time when this record was added.
 * @property note A note associated with this record.
 */
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

/**
 * Represents a Notification entity in the database.
 *
 * @property id The unique ID of the notification.
 * @property projectId The ID of the project that this notification is associated with.
 * @property notificationType The type of the notification (specific time, random).
 * @property time The time associated with the notification.
 */
@Entity(
    foreignKeys = [ForeignKey(
        entity = Project::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("projectId"),
        onDelete = ForeignKey.CASCADE
    )],
)
data class ProjNotification(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val projectId: Int,
    val notificationType: String,
    val time: String,
)