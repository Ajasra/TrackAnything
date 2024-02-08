package com.example.trackanything.receivers

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.trackanything.R
import com.example.trackanything.models.Project
import com.example.trackanything.activities.ProjectActivity
import com.example.trackanything.utils.NotificationUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * This class extends BroadcastReceiver and is used to receive broadcasts for alarms.
 * When an alarm goes off, a notification is created and displayed.
 */
class AlarmReceiver : BroadcastReceiver() {
    companion object {
        /**
         * The request code for the POST_NOTIFICATIONS permission.
         */
        const val REQUEST_CODE = 100
    }

    /**
     * This method is called when the BroadcastReceiver is receiving an Intent broadcast.
     * It creates and displays a notification.
     *
     * @param context The Context in which the receiver is running.
     * @param intent The Intent received.
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onReceive(context: Context, intent: Intent) {
        // Get the project from the intent
        val project: Project = intent.getParcelableExtra("project")!!

        // Create an intent for the ProjectActivity
        val notificationIntent = Intent(context, ProjectActivity::class.java).apply {
            // Put the project id as an extra
            putExtra("project_id", project.id)
            // Set the flags for the intent
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        // Create a pending intent that will be triggered when the notification is clicked
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

        // Build the notification
        val notification = NotificationCompat.Builder(context, NotificationUtils.CHANNEL_ID)
            .setContentTitle("Add your record")
            .setContentText("You have a reminder for project ${project.name}")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .build()

        // Get the notification manager
        val notificationManager = NotificationManagerCompat.from(context)

        // Check if the POST_NOTIFICATIONS permission is granted
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // If the permission is not granted, request it
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_CODE
            )
            return
        }

        // Display the notification
        notificationManager.notify(project.id, notification)

        // Handle the next notification
        GlobalScope.launch(Dispatchers.IO) {
            NotificationUtils.handleNextNotification(context)
        }
    }
}