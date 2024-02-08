package com.example.trackanything.utils

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.trackanything.R
import com.example.trackanything.models.Project
import com.example.trackanything.receivers.AlarmReceiver
import android.net.Uri
import android.provider.Settings
import com.example.trackanything.database.AppDatabase
import com.example.trackanything.models.ProjNotification
import com.example.trackanything.repository.NotificationRepository
import com.example.trackanything.repository.ProjectRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * This object contains utility methods for creating and managing notifications.
 */
object NotificationUtils {
    public const val CHANNEL_ID = "track_anything_channel"

    /**
     * Creates a notification channel.
     * This is required for notifications on Android O (API 26) and above.
     *
     * @param context The application context.
     */
    fun createNotificationChannel(context: Context) {
        // Check if the Android version is greater than or equal to O
        // Get the channel name and description from the string resources
        val name = context.getString(R.string.channel_name)
        val descriptionText = context.getString(R.string.channel_description)
        // Set the importance level of the notification
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        // Create the notification channel with the specified parameters
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        // Get the notification manager and create the notification channel
        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    /**
     * Schedules a notification to be shown at a specific time.
     *
     * @param context The application context.
     * @param project The project for which the notification is to be scheduled.
     * @param timeInMillis The time at which the notification should be shown, in milliseconds.
     */
    private fun scheduleNotification(context: Context, project: Project, timeInMillis: Long) {
        // Get the alarm manager service
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Create an intent for the alarm receiver and put the project as an extra
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra("project", project)
        }

        // Create a pending intent that will be triggered when the alarm goes off
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            project.id,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Check if the Android version is greater than or equal to S
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // If the app can schedule exact alarms, set the alarm
            if (alarmManager.canScheduleExactAlarms()) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
            } else {
                // If the app cannot schedule exact alarms, request the SCHEDULE_EXACT_ALARM permission
                val permissionIntent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                    data = Uri.fromParts("package", context.packageName, null)
                }
                context.startActivity(permissionIntent)
            }
        } else {
            // If the Android version is less than S, set the alarm
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent)
        }
    }

    /**
     * Checks if any notifications are scheduled for the provided list of projects.
     *
     * @param context The application context.
     * @param projects The list of projects to check.
     * @return True if any notifications are scheduled, false otherwise.
     */
    private suspend fun areAnyNotificationsScheduled(context: Context, projects: List<Project>): Boolean {
        for (project in projects) {
            if (isNotificationScheduled(context, project.id)) {
                return true
            }
        }
        return false
    }

    /**
     * Checks if a notification is scheduled for a specific project.
     *
     * @param context The application context.
     * @param requestCode The request code of the project.
     * @return True if a notification is scheduled, false otherwise.
     */
    private fun isNotificationScheduled(context: Context, requestCode: Int): Boolean {
        val intent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        return if(pendingIntent != null){
            println("Notification scheduled for project: $requestCode")
            true
        }else{
            false
        }
    }


    // WORK WITH DATABASE

    /**
     * Checks for scheduled notifications at the start of the application.
     * If no notifications are scheduled, it handles the next notification.
     *
     * @param context The application context.
     * @param projectRepository The repository for accessing project data.
     */
    suspend fun checkNotificationOnStart(context: Context, projectRepository: ProjectRepository) {
        val projects = projectRepository.getAll()
        if(!areAnyNotificationsScheduled(context, projects)) {
            println("No notification scheduled")
            handleNextNotification(context)
        }
    }

    /**
     * Handles the next notification.
     * It deletes older notifications and schedules the next notification from the database.
     * If no notifications are in the database, it schedules notifications for all projects.
     *
     * @param context The application context.
     */
    suspend fun handleNextNotification(context: Context) {
        println("Handling next notification")
        deleteOlderNotifications(context, Helpers.getCurrentTime().toString())
        var nextNotification = getNextNotificationFromDatabase(context)

        val projectDao = AppDatabase.getDatabase(context).projectDao()
        val projectRepository = ProjectRepository(projectDao)
        withContext(Dispatchers.IO) {
            if (nextNotification != null) {
                println("Next notification: $nextNotification")
                val project = projectRepository.getById(nextNotification!!.projectId)
                scheduleNotification(context, project, nextNotification!!.time.toLong())
            } else {
                println("No notifications in database")
                val projects = projectRepository.getAll()
                scheduleAllNotifications(context, projects)
                nextNotification = getNextNotificationFromDatabase(context)
                if (nextNotification != null) {
                    println("Next notification: $nextNotification")
                    val project = projectRepository.getById(nextNotification!!.projectId)
                    scheduleNotification(context, project, nextNotification!!.time.toLong())
                }
            }
        }
    }


    /**
     * Adds a new notification to the database.
     *
     * @param context The application context.
     * @param project The project for which the notification is to be added.
     * @param time The time at which the notification should be shown, in milliseconds.
     * @param message The message to be displayed in the notification.
     * @param title The title of the notification.
     */
    private suspend fun addNotificationToDB(context: Context, project: Project, time: String, message: String = "", title: String = "Add your record") {
        val notificationDao = AppDatabase.getDatabase(context).notificationDao()
        val notificationRepository = NotificationRepository(notificationDao)

        val projNotification = ProjNotification(
            id = 0,
            projectId = project.id,
            time = time,
            message = message,
            title = title)
        withContext(Dispatchers.IO) {
            notificationRepository.insert(projNotification)
            println("Notification added to DB: $projNotification")
        }
    }

    /**
     * Retrieves the next notification from the database.
     *
     * @param context The application context.
     * @return The next [ProjNotification] object from the database, or null if no notifications are available.
     */
    private suspend fun getNextNotificationFromDatabase(context: Context): ProjNotification? {
        val notificationDao = AppDatabase.getDatabase(context).notificationDao()
        val notificationRepository = NotificationRepository(notificationDao)
        return withContext(Dispatchers.IO) {
            return@withContext notificationRepository.getNext()
        }
    }

    /**
     * Deletes a notification from the database by its ID.
     *
     * @param context The application context.
     * @param id The ID of the notification to be deleted.
     */
    suspend fun deleteNotificationFromDB(context: Context, id: Int) {
        println("Delete notifications: $id")
        val notificationDao = AppDatabase.getDatabase(context).notificationDao()
        val notificationRepository = NotificationRepository(notificationDao)
        withContext(Dispatchers.IO) {
            try {
                notificationRepository.delete(id)
            } catch (e: Exception) {
                println("Error deleting notification: $e")
            }
        }
    }

    /**
     * Deletes all notifications for a specific project from the database.
     *
     * @param context The application context.
     * @param projectId The ID of the project for which notifications should be deleted.
     */
    suspend fun deleteNotificationForProject(context: Context, projectId: Int) {
        println("Delete notification for project: $projectId")
        val notificationDao = AppDatabase.getDatabase(context).notificationDao()
        val notificationRepository = NotificationRepository(notificationDao)
        withContext(Dispatchers.IO) {
            try {
                notificationRepository.deletaByProjectId(projectId)
            } catch (e: Exception) {
                println("Error deleting notification: $e")
            }
        }
    }

    /**
     * Deletes all notifications from the database.
     *
     * @param context The application context.
     */
    suspend fun deleteNotificationsAll(context: Context){
        val notificationDao = AppDatabase.getDatabase(context).notificationDao()
        val notificationRepository = NotificationRepository(notificationDao)
        withContext(Dispatchers.IO) {
            try {
                notificationRepository.deleteAll()
            } catch (e: Exception) {
                println("Error deleting notification: $e")
            }
        }
    }

    /**
     * Deletes notifications that are older than the specified time.
     *
     * @param context The application context.
     * @param time The time in milliseconds. Notifications older than this time will be deleted.
     */
    private suspend fun deleteOlderNotifications(context: Context, time: String) {
        println("Delete old notifications")
        val notificationDao = AppDatabase.getDatabase(context).notificationDao()
        val notificationRepository = NotificationRepository(notificationDao)
        withContext(Dispatchers.IO) {
            try {
                notificationRepository.deleteOlderThan(time)
            } catch (e: Exception) {
                println("Error deleting notification: $e")
            }
        }
    }

    /**
     * Retrieves a notification from the database by its ID.
     *
     * @param context The application context.
     * @param id The ID of the notification to be retrieved.
     * @return The [ProjNotification] object with the specified ID, or null if no such notification exists.
     */
    suspend fun getNotificationById(context: Context, id: Int): ProjNotification {
        val notificationDao = AppDatabase.getDatabase(context).notificationDao()
        val notificationRepository = NotificationRepository(notificationDao)
        return withContext(Dispatchers.IO) {
            return@withContext notificationRepository.getById(id)
        }
    }

    /**
     * Schedules notifications for a specific project.
     * If the project has a notification time set, it will schedule notifications according to the project's notification type.
     * If the notification type is "Random", it will schedule notifications at random times within the specified range.
     * If the notification type is not "Random", it will schedule notifications at the times specified in the notification time.
     *
     * @param context The application context.
     * @param project The project for which notifications should be scheduled.
     */
    suspend fun scheduleNotificationsForProject(context: Context, project: Project) {
        println("Adding notification for project: $project")
        deleteNotificationForProject(context, project.id)
        val notificationTime = project.notificationTime
        if (notificationTime != "") {
            if (project.notificationType == "Random") {
                val tmpTime = Helpers.getTimeInARange(notificationTime)
                for (time in tmpTime) {
                    println("Time: $time")
                    addNotificationToDB(context, project, time.toString())
                }
            } else {
                val timeArray = notificationTime.split(",")
                for (time in timeArray) {
                    println("Time: $time")
                    val tmpTime = Helpers.convertTimeToTimestamp(time)
                    addNotificationToDB(context, project, tmpTime.toString())
                }
            }
        }
    }

    /**
     * Schedules notifications for all projects.
     * This function iterates over all projects and calls the function to schedule notifications for each project.
     *
     * @param context The application context.
     * @param projects The list of projects for which notifications should be scheduled.
     */
    private suspend fun scheduleAllNotifications(context: Context, projects: List<Project>) {
        println("Adding notifications for all projects")
        for (project in projects) {
            scheduleNotificationsForProject(context, project)
        }
    }

    //TODO:
    // I need to check the option if no more notifications for today,
    // then i adding them for the next day, now i still need to open
    // app at a new day first to do it
}