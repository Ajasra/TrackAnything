package com.example.trackanything.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

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
 * @property notificationType A string representing the type of notifications associated with the project.
 * @property notificationTime A string representing the time of notifications associated with the project.
 */
@Parcelize
@Entity
data class Project(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val description: String,
    val active: Boolean,
    val tags: String,
    val valueType: String,
    val options: String,
    val notificationType: String,
    val notificationTime: String,
) : Parcelable

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
 * @property time The time when this notification is scheduled.
 * @property message The message of the notification.
 * @property title The title of the notification.
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
    val time: String,
    val message: String,
    val title: String,
)