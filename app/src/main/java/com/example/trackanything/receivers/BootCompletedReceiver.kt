package com.example.trackanything.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.trackanything.database.AppDatabase
import com.example.trackanything.repository.ProjectRepository
import com.example.trackanything.utils.NotificationUtils
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * This class extends BroadcastReceiver and is used to receive broadcasts when the device boots up.
 * When the device boots up, it checks if the intent action is "android.intent.action.BOOT_COMPLETED".
 * If it is, it performs the necessary actions.
 */
class BootCompletedReceiver : BroadcastReceiver() {
    /**
     * This method is called when the BroadcastReceiver is receiving an Intent broadcast.
     * It checks if the intent action is "android.intent.action.BOOT_COMPLETED".
     * If it is, it performs the necessary actions.
     *
     * @param context The Context in which the receiver is running.
     * @param intent The Intent received.
     */
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            val projectDao = AppDatabase.getDatabase(context).projectDao()
            val projectRepository = ProjectRepository(projectDao)

            GlobalScope.launch {
                NotificationUtils.checkNotificationOnStart(context, projectRepository)
            }
        }
    }
}