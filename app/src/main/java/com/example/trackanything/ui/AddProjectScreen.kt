package com.example.trackanything.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trackanything.model.Entities.ProjNotification
import com.example.trackanything.model.Entities.Project
import com.example.trackanything.repository.NotificationRepository
import com.example.trackanything.repository.ProjectRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AddProjectScreen(navController: NavController, projectRepository: ProjectRepository, notificationRepository: NotificationRepository) {
    var projectName by remember { mutableStateOf("") }
    var projectDescription by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    val valueTypes = listOf("number", "check", "select", "text")
    var selectedValueType by remember { mutableStateOf(valueTypes[0]) }
    var showDropdown by remember { mutableStateOf(false) }
    var selectValues by remember { mutableStateOf("") }

    val notificationType = listOf("Random", "Specific")
    var selectedNotificationType by remember { mutableStateOf(notificationType[0]) }
    var showNotificationTypeDropdown by remember { mutableStateOf(false) }
    var notificationValue by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = projectName,
            onValueChange = { projectName = it },
            label = { Text("Project Name") },
            modifier = Modifier.fillMaxWidth(),
        )
        if (showError) {
            Text(
                text = "Project name is required",
                color = Color.Red,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = projectDescription,
            onValueChange = { projectDescription = it },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { showDropdown = true }) {
            Text("Value Type: $selectedValueType")
        }

        DropdownMenu(
            expanded = showDropdown,
            onDismissRequest = { showDropdown = false }
        ) {
            valueTypes.forEach { valueType ->
                DropdownMenuItem(
                    text = { Text(valueType) },
                    onClick = {
                        selectedValueType = valueType
                        showDropdown = false
                    }
                )

            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedValueType == "select") {
            OutlinedTextField(
                value = selectValues,
                onValueChange = { selectValues = it },
                label = { Text("Enter values (comma separated)") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

//        Add here fields for notification type and time

        Button(onClick = { showNotificationTypeDropdown = true }) {
            Text("Notification Type: $selectedNotificationType")
        }

        DropdownMenu(
            expanded = showNotificationTypeDropdown,
            onDismissRequest = { showNotificationTypeDropdown = false }
        ) {
            notificationType.forEach { type ->
                DropdownMenuItem(
                    text = { Text(type) },
                    onClick = {
                        selectedNotificationType = type
                        showNotificationTypeDropdown = false
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = notificationValue,
            onValueChange = { notificationValue = it },
            label = { Text("Enter time") },
            modifier = Modifier.fillMaxWidth()
        )

        if(selectedNotificationType == "Random"){
            Text(
                text = "Format: HH:MM-HH:MM, Number of times",
                modifier = Modifier.padding(top = 4.dp)
            )
        }else{
            Text(
                text = "Format: HH:MM, HH:MM, HH:MM ...",
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Button(onClick = {
            if (projectName.isBlank()) {
                showError = true
            } else {
                showError = false
                val newProject = Project(
                    id = 0, // 0 because the id is auto-generated
                    name = projectName,
                    description = projectDescription,
                    active = true,
                    tags = "",
                    valueType = selectedValueType,
                    options = selectValues
                )

                CoroutineScope(Dispatchers.IO).launch {
                    val projectId = projectRepository.insert(newProject).toInt()
                    val newNotification = ProjNotification(
                        id = 0, // 0 because the id is auto-generated
                        projectId = projectId, // Convert to Int if necessary
                        notificationType = selectedNotificationType,
                        time = notificationValue,
                    )
                    notificationRepository.insert(newNotification)
                    println("Notification added: $newNotification")
                    withContext(Dispatchers.Main) {
                        println("Project added: $newProject")
                        navController.popBackStack()
                    }
                }
                println("Project added: $newProject")
                navController.popBackStack()
            }
        }) {
            Text("Add Project")
        }
    }
}