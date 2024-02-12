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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.*
import com.example.trackanything.repository.ProjectRepository
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trackanything.models.Record
import com.example.trackanything.repository.RecordRepository
import com.example.trackanything.utils.Helpers.exportProjectRecords
import com.example.trackanything.utils.NotificationUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectScreen(navController: NavController, projectRepository: ProjectRepository, recordRepository: RecordRepository, projectId: String) {

    val project = projectRepository.getLDById(projectId.toInt()).observeAsState(initial = null).value
    val records = recordRepository.getRecordsForProject(projectId.toInt()).observeAsState(initial = emptyList()).value

    val showDialog = remember { mutableStateOf(false) }

    val context = LocalContext.current

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
                        exportProjectRecords(context, records, project?.name ?: "project")
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), // Adjust this padding as needed
            verticalArrangement = Arrangement.Top
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
                    .padding(top = 60.dp, start = 16.dp, end = 16.dp, bottom = 0.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                LazyColumn(modifier = Modifier
                    .fillMaxWidth()
                ) {
                    items(records) { record ->
                        RecordItem(record) // Display each project
                    }
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