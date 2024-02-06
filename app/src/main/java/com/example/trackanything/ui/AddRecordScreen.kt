package com.example.trackanything.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.material3.Snackbar
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trackanything.repository.ProjectRepository
import com.example.trackanything.repository.RecordRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun AddRecordScreen(navController: NavController, projectRepository: ProjectRepository, recordRepository: RecordRepository, projectId: String) {

    val project = projectRepository.getById(projectId.toInt()).observeAsState(initial = null).value

    var value by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var showSnackbar by remember { mutableStateOf(false) } // Add this line
    var isButtonEnabled by remember { mutableStateOf(true) } // Add this line

    var errorText by remember { mutableStateOf("") }

    Column( modifier = Modifier
        .fillMaxSize()
        .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = project?.name ?: "")
        Text(text = project?.description ?: "")

        when (project?.valueType) {
            "number" -> {
                OutlinedTextField(
                    value = value,
                    onValueChange = { value = it },
                    label = { Text("Enter a number") },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            "check" -> {
                var checkedState by remember { mutableStateOf(false) }
                Checkbox(
                    checked = checkedState,
                    onCheckedChange = { checkedState = it; value = it.toString() },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            "select" -> {
                var selectedOption by remember { mutableStateOf("") }
                var showDropdown by remember { mutableStateOf(false) }

                val options = project.options.split(",")

                Button(onClick = { showDropdown = true }) {
                    Text(text = selectedOption.ifBlank { "Select an option" })
                }

                DropdownMenu(
                    expanded = showDropdown,
                    onDismissRequest = { showDropdown = false }
                ) {
                    options.forEach { option ->
                        DropdownMenuItem(onClick = {
                            selectedOption = option.trim()
                            showDropdown = false
                            value = option.trim()
                        },
                            text = {
                                Text(option.trim())
                            })
                        }
                    }

            }
            "text" -> {
                OutlinedTextField(
                    value = value,
                    onValueChange = { value = it },
                    label = { Text("Enter text") },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        if (showError) {
            Text(
                text = errorText,
                color = Color.Red,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        OutlinedTextField(
            value = note,
            onValueChange = { note = it },
            label = { Text("Note") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                if (value.isBlank()) {
                    showError = true
                    errorText = "Value is required"
                } else {

                    if (project?.valueType == "number") {
                        if (!value.matches(Regex("^[0-9]+(\\.[0-9]+)?$"))) {
                            showError = true
                            errorText = "Invalid number"
                            return@Button
                        }
                    }

                    showError = false
                    isButtonEnabled = false
                    val newRecord = com.example.trackanything.model.Entities.Record(
                        id = 0,
                        projectId = projectId.toInt(),
                        value = value,
                        note = note,
                        timeAdded = System.currentTimeMillis()
                    )
                    CoroutineScope(Dispatchers.IO).launch {
                        recordRepository.insert(newRecord)
                        showSnackbar = true
                        kotlinx.coroutines.delay(2000) // delay for 2 seconds
                        showSnackbar = false
                        withContext(Dispatchers.Main) {
                            navController.popBackStack() // Move this line here
                        }
                    }
                    println("Record added: $newRecord")

                }
            },
            enabled = isButtonEnabled // Use the state variable here
        ) {
            Text("Add Record")
        }

        // Add this block
        if (showSnackbar) {
            Snackbar(
                modifier = Modifier.align(Alignment.End),
                action = {
                    TextButton(onClick = { showSnackbar = false }) {
                        Text(text = "Dismiss")
                    }
                }
            ) {
                Text(text = "Record added successfully")
            }
        }
    }
}