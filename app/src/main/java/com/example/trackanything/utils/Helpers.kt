package com.example.trackanything.utils

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.navigation.NavController
import java.io.OutputStreamWriter
import java.util.Calendar
import com.example.trackanything.models.Record
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * This class contains utility methods for the application.
 */
object Helpers {

    /**
     * Converts a date string to a long value.
     *
     * @param date The date string to be converted.
     * @return The long value of the date string.
     */
    fun convertDateToTimestamp(date: String): Long {
        val dateArray = date.split(" ")
        val timeArray = dateArray[0].split(":")
        val dayArray = dateArray[1].split(":")
        val time = timeArray[0].toInt() * 60 * 60 * 1000 + timeArray[1].toInt() * 60 * 1000
        val day = dayArray[0].toInt() * 24 * 60 * 60 * 1000 + dayArray[1].toInt() * 60 * 60 * 1000 + dayArray[2].toInt() * 60 * 1000
        return (time + day).toLong()
    }


    /**
     * Retrieves the current time in milliseconds.
     *
     * @return The current time in milliseconds since the Unix epoch (January 1, 1970, 00:00:00 GMT).
     */
    fun getCurrentTime(): Long {
        val calendar = Calendar.getInstance()
        return calendar.timeInMillis
    }

    /**
     * Converts a time string to a timestamp for a current day
     *
     * @param time The time string to be converted.
     * @return The timestamp of the time string.
     */
    fun convertTimeToTimestamp(time: String): Long {
        val tmpTime = time.split(":")
        val hour = tmpTime[0].toInt()
        val minute = tmpTime[1].toInt()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        if (calendar.timeInMillis < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return calendar.timeInMillis
    }

    /**
     * Gets the time in a range. in format "hh:mm-hh:mm,N"
     * where N how many values to generate
     *
     * @param time The time string to be converted.
     * @return The list of long values of the time string.
     */
    fun getTimeInARange(time: String): List<Long> {
        println(time)
        val timeArray = time.split(",")
        val timeRange = timeArray[0].split("-")
        val timeStart = timeRange[0].split(":")
        val timeEnd = timeRange[1].split(":")
        val timeList = mutableListOf<Long>()
        val startHour = timeStart[0].toInt()
        val startMinute = timeStart[1].toInt()
        val endHour = timeEnd[0].toInt()
        val endMinute = timeEnd[1].toInt()
        val count = timeArray[1].toInt()
        for (i in 0 until count) {

            val rand = (endHour-startHour) * 60 + (endMinute - startMinute)
            val rn = (0..rand).random() + startMinute
            val hour = (rn / 60).toInt() + startHour
            var minute = rn % 60
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            if (calendar.timeInMillis < System.currentTimeMillis()) {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }
            timeList.add(calendar.timeInMillis)
        }
        return timeList
    }


    /**
     * Exports the project records to a file.
     *
     * This function creates a new file with the name of the project in the Downloads directory,
     * writes the records to this file, and returns the path of the created file.
     *
     * @param context The context to use for content resolution.
     * @param records The list of records to be written to the file.
     * @param projectName The name of the project, which will be used as the file name.
     * @return The path of the created file, or null if the file could not be created.
     */
    fun exportProjectRecords(context: Context, records: List<Record>, projectName: String): String? {
        val relativePath = Environment.DIRECTORY_DOWNLOADS
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "$projectName.txt")
            put(MediaStore.MediaColumns.MIME_TYPE, "text/plain")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
            }
        }

        val uri = context.contentResolver.insert(MediaStore.Files.getContentUri("external"), contentValues)

        uri?.let {
            context.contentResolver.openOutputStream(it)?.let { outputStream ->
                val writer = OutputStreamWriter(outputStream)
                for (record in records) {
                    writer.write("${record.value}\t${record.note}\n")
                }
                writer.close()
            }
        }

        return uri?.path
    }

    /**
     * Finishes the current activity and navigates to the main screen.
     *
     * This function finishes the current activity and navigates to the main screen using the provided NavController.
     * If the NavController does not have a current destination, it simply finishes the current activity.
     *
     * @param coroutineScope The CoroutineScope to use for launching the coroutine.
     * @param activity The current activity, which will be finished.
     * @param navController The NavController to use for navigation.
     */
    fun finishActivity(coroutineScope: CoroutineScope, activity: Activity?, navController: NavController) {
        coroutineScope.launch {
            // Finish the current activity
            activity?.finish()
            // Check if NavController has a current destination before navigating
            if (navController.currentDestination != null) {
                navController.navigate("main_screen")
            }else{
                activity?.finish()
            }
        }
    }

}
