package com.example.trackanything.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.*
import com.example.trackanything.repository.ProjectRepository
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trackanything.model.Entities.ProjNotification
import com.example.trackanything.model.Entities.Project
import com.example.trackanything.repository.NotificationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun EditProjectScreen(navController: NavController, projectRepository: ProjectRepository, notificationRepository: NotificationRepository, projectId: String) {

    val project = projectRepository.getById(projectId.toInt()).observeAsState(initial = null).value
    val projNotification = notificationRepository.getByProjectId(projectId.toInt()).observeAsState(initial = null).value


    if (project != null && projNotification != null) {
        var projectName by remember { mutableStateOf(project.name) }
        var projectDescription by remember { mutableStateOf(project.description) }
        var projectActive by remember { mutableStateOf(project.active) }
        var projectTags by remember { mutableStateOf(project.tags) }
        var projectValueType by remember { mutableStateOf(project.valueType) }
        var projectOptions by remember { mutableStateOf(project.options) }

        var notificationType by remember { mutableStateOf(projNotification.notificationType) }
        var notificationValue by remember { mutableStateOf(projNotification.time) }
        val notificationId by remember { mutableIntStateOf(projNotification.id) }

        var showDropdown by remember { mutableStateOf(false) }
        val valueTypes = listOf("number", "check", "select", "text")
        var showNotificationTypeDropdown by remember { mutableStateOf(false) }
        val notificationTypeList = listOf("Random", "Specific")

        Column(modifier = Modifier.fillMaxSize()) {
            MyHeader(title = "Edit Project")

            Spacer(modifier = Modifier.padding(16.dp))
            OutlinedTextField(
                value = projectName,
                onValueChange = { projectName = it },
                label = { Text("Project Name") },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.padding(16.dp))
            OutlinedTextField(
                value = projectDescription,
                onValueChange = { projectDescription = it },
                label = { Text("Project Description") },
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.padding(16.dp))
            OutlinedTextField(
                value = projectTags,
                onValueChange = { projectTags = it },
                label = { Text("Project Tags") },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.padding(16.dp))

            Button(onClick = { showDropdown = true }) {
                Text("Value Type: $projectValueType")
            }

            DropdownMenu(
                expanded = showDropdown,
                onDismissRequest = { showDropdown = false }
            ) {
                valueTypes.forEach { valueType ->
                    DropdownMenuItem(
                        text = { Text(valueType) },
                        onClick = {
                            projectValueType = valueType
                            showDropdown = false
                        }
                    )

                }
            }
            Spacer(modifier = Modifier.padding(16.dp))

            if (projectValueType == "select") {
                OutlinedTextField(
                    value = projectOptions,
                    onValueChange = { projectOptions = it },
                    label = { Text("Options") },
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.padding(16.dp))
            }

            Button(onClick = { showNotificationTypeDropdown = true }) {
                Text("Notification Type: $notificationType")
            }

            DropdownMenu(
                expanded = showNotificationTypeDropdown,
                onDismissRequest = { showNotificationTypeDropdown = false }
            ) {
                notificationTypeList.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type) },
                        onClick = {
                            notificationType = type
                            showNotificationTypeDropdown = false
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = notificationValue,
                onValueChange = { notificationValue = it },
                label = { Text("Notification Value") },
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(modifier = Modifier.height(16.dp))

            Checkbox(
                checked = projectActive,
                onCheckedChange = { projectActive = it },
                modifier = Modifier.padding(16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                updateProject(
                    projectRepository,
                    notificationRepository,
                    projectId.toInt(),
                    projectName,
                    projectDescription,
                    projectActive,
                    projectTags,
                    projectValueType,
                    projectOptions,
                    notificationType,
                    notificationValue,
                    notificationId
                )
                navController.popBackStack()
            }) {
                Text("Save")
            }
        }
    }else{
        CircularProgressIndicator()
    }
}

fun updateProject(
    projectRepository: ProjectRepository,
    notificationRepository: NotificationRepository,
    projectId: Int,
    projectName: String,
    projectDescription: String,
    projectActive: Boolean,
    projectTags: String,
    projectValueType: String,
    projectOptions: String,
    notificationType: String,
    notificationValue: String,
    notificationId: Int
) {
    val updProject = Project(
        id = projectId,
        name = projectName,
        description = projectDescription,
        active = projectActive,
        tags = projectTags,
        valueType = projectValueType,
        options = projectOptions
    )

    CoroutineScope(Dispatchers.IO).launch {
        projectRepository.update(updProject)
        val newNotification = ProjNotification(
            id = notificationId,
            projectId = projectId, // Convert to Int if necessary
            notificationType = notificationType,
            time = notificationValue,
        )
        notificationRepository.update(newNotification)
        println("Notification added: $newNotification")
    }
}