package com.example.trackanything.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.*
import com.example.trackanything.repository.ProjectRepository
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.example.trackanything.models.Record
import com.example.trackanything.repository.RecordRepository
import com.example.trackanything.utils.Helpers
import com.example.trackanything.utils.Helpers.exportProjectRecords
import com.example.trackanything.utils.NotificationUtils
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectScreen(navController: NavController, projectRepository: ProjectRepository, recordRepository: RecordRepository, projectId: String) {

    val project = projectRepository.getLDById(projectId.toInt()).observeAsState(initial = null).value
    val allRecords = recordRepository.getRecordsForProject(projectId.toInt()).observeAsState(initial = emptyList()).value

    val showDialog = remember { mutableStateOf(false) }
    var viewMode by remember { mutableStateOf("day") }

    // get timezone offset
    val timeZone = TimeZone.getDefault()
    val hourOffset =timeZone.rawOffset / 1000 / 60 / 60

    val calendar = Calendar.getInstance()
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    var selectedDay by remember { mutableLongStateOf(calendar.timeInMillis) }
    calendar.set(Calendar.DAY_OF_WEEK, calendar.firstDayOfWeek)
    var selectedWeek by remember { mutableLongStateOf(calendar.timeInMillis) }
    var selectedTime by remember { mutableStateOf<Float?>(null) }


    val context = LocalContext.current

    val records = if (viewMode == "day") {
        recordRepository.getRecordsForProjectAndTime(projectId.toInt(), selectedDay, selectedDay + 24 * 60 * 60 * 1000).observeAsState(initial = emptyList()).value
    } else {
        recordRepository.getRecordsForProjectAndTime(projectId.toInt(), selectedWeek, selectedWeek + 7 * 24 * 60 * 60 * 1000).observeAsState(initial = emptyList()).value
    }

    val groupedRecords = records.groupBy {
        val tmpcalendar = Calendar.getInstance().apply {
            timeInMillis = it.timeAdded
        }
        if (viewMode == "day") {
            tmpcalendar.get(Calendar.HOUR_OF_DAY)
        } else {
            tmpcalendar.get(Calendar.DAY_OF_WEEK)
        }
    }
    val entries = groupedRecords.map { (time, records) ->
        BarEntry(time.toFloat(), records.size.toFloat())
    }

    val label = if (viewMode == "day") "Hour" else "Day"

    val dataSet = BarDataSet(entries, label) // add entries to dataset
    val barData = BarData(dataSet)

    val dateFormat = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
    val hourFormat = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
    val dayFormat = java.text.SimpleDateFormat("dd/MM", java.util.Locale.getDefault())




    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = false },
            title = { Text("Confirm Deletion") },
            text = { Text("Are you sure you want to delete this project and all its records?") },
            confirmButton = {
                TextButton(onClick = {
                    deleteProject(context, projectRepository, recordRepository, projectId, navController) // Call the function here
                    showDialog.value = false
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog.value = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(text = "TrackAnything") },
            navigationIcon = {
                IconButton(onClick = {
                    navController.navigate("main_screen") // Navigate to the main screen
                }) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(
                    onClick = {
                        exportProjectRecords(context, allRecords, project?.name ?: "project")
                        Toast.makeText(context, "File saved at Downloads/${project?.name}.txt", Toast.LENGTH_LONG).show()
                    }) {
                    Icon(Icons.Filled.Share, contentDescription = "Export")
                }
                IconButton(
                    onClick = {
                        if (project != null) {
                            navController.navigate("editProject/${project.id}")
                        }
                    }) {
                    Icon(Icons.Filled.Edit, contentDescription = "Edit")
                }
                IconButton(
                    onClick = {
                        showDialog.value = true
                    }) {
                    Icon(Icons.Filled.Delete, contentDescription = "Delete")
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 16.dp, end = 16.dp, bottom = 0.dp),
        ) {
            Text(text = "Project: ${project?.name ?: ""}", style = MaterialTheme.typography.h6)
            Text(
                text = "Description: ${project?.description ?: ""}",
                style = MaterialTheme.typography.body2
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                if (viewMode == "day") {
                    selectedDay -= 24 * 60 * 60 * 1000
                } else {
                    selectedWeek -= 7 * 24 * 60 * 60 * 1000
                }
            }) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Previous")
            }

            Text(text = if (viewMode == "day") {
                dateFormat.format(java.util.Date(selectedDay))
            } else {
                "${dateFormat.format(java.util.Date(selectedWeek))} - ${dateFormat.format(java.util.Date(selectedWeek + 7 * 24 * 60 * 60 * 1000))}"
            })

            IconButton(onClick = {
                if (viewMode == "day") {
                    selectedDay += 24 * 60 * 60 * 1000
                } else {
                    selectedWeek += 7 * 24 * 60 * 60 * 1000
                }
            }) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next")
            }

            androidx.compose.material.Switch(
                checked = viewMode == "week",
                onCheckedChange = { isChecked ->
                    viewMode = if (isChecked) "week" else "day"
                }
            )
        }

        if(records != null && records.isNotEmpty()){
            AndroidView(factory = { context ->
                BarChart(context).apply {

                    setOnChartValueSelectedListener(object : com.github.mikephil.charting.listener.OnChartValueSelectedListener {
                        override fun onValueSelected(e: Entry?, h: com.github.mikephil.charting.highlight.Highlight?) {
                            selectedTime = e?.x
                        }

                        override fun onNothingSelected() {
                            selectedTime = null
                        }
                    })

                    description.isEnabled = false

                    xAxis.valueFormatter = object : com.github.mikephil.charting.formatter.ValueFormatter() {
                        override fun getFormattedValue(value: Float): String {
                            return if (viewMode == "day") {
                                hourFormat.format(java.util.Date(value.toLong() * 60 * 60 * 1000))
                            } else {
                                dayFormat.format(java.util.Date((value.toLong() + selectedWeek / (24 * 60 * 60 * 1000)) * 24 * 60 * 60 * 1000))
                            }
                        }
                    }

                    data = barData
                    xAxis.axisMinimum = if (viewMode == "day") 0f - hourOffset else .5f // start at zero - hourOffset for day view and zero for week view
                    xAxis.axisMaximum = if (viewMode == "day") 24f - hourOffset else 7.5f // the axis maximum is 24 - hourOffset for day view and 7 for week view
                    invalidate() // refreshes the chart
                }
            }, modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), // Adjust this padding as needed
            verticalArrangement = Arrangement.Top
        ) {

                LazyColumn(modifier = Modifier
                    .fillMaxWidth()
                ) {
                    items(records.filter { record ->
                        if (selectedTime != null) {
                            val tmpcalendar = Calendar.getInstance().apply {
                                timeInMillis = record.timeAdded
                            }
                            val time = if (viewMode == "day") {
                                tmpcalendar.get(Calendar.HOUR_OF_DAY).toFloat()
                            } else {
                                tmpcalendar.get(Calendar.DAY_OF_WEEK).toFloat()
                            }
                            time == selectedTime
                        } else {
                            true
                        }
                    }) { record ->
                        RecordItem(record) // Display each project
                    }
                }
            }

    }
}

@Composable
fun RecordItem(record: Record) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // each rowe is the timeAdded, value and note if there
        // timeAdded converted from long (milliseconds) to format "d/m/y h:m"
        Text(text = Helpers.convertLongToDateString(record.timeAdded))
        Text(text = record.value)
        if(record.note.isNotEmpty()){
            Spacer(modifier = Modifier.fillMaxWidth(0.1f))
            Text(
                text = record.note
            )
        }
    }
}

fun deleteProject(context: Context, projectRepository: ProjectRepository, recordRepository: RecordRepository, projectId: String, navController: NavController){
    CoroutineScope(Dispatchers.IO).launch {
        recordRepository.deleteForProject(projectId.toInt())
        projectRepository.delete(projectId.toInt())
        NotificationUtils.deleteNotificationForProject(context, projectId.toInt())
        withContext(Dispatchers.Main) {
            navController.popBackStack() // Move this line here
        }
    }
}