package com.example.trackanything.receivers

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * NotificationReceiver is a BroadcastReceiver that cancels a notification when it receives an intent.
 * The intent should contain the id of the notification to be cancelled in an extra named "notificationId".
 */
class NotificationReceiver : BroadcastReceiver() {

    /**
     * This method is called when the BroadcastReceiver is receiving an Intent broadcast.
     * It retrieves the notification id from the intent extras and cancels the notification with this id.
     * @param context The Context in which the receiver is running.
     * @param intent The Intent received.
     */
    override fun onReceive(context: Context, intent: Intent) {
        // Retrieve the notification id from the intent extras
        val notificationId = intent.getIntExtra("notificationId", 0)

        // Get the NotificationManager from the context
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Cancel the notification with the retrieved id
        notificationManager.cancel(notificationId)
    }
}