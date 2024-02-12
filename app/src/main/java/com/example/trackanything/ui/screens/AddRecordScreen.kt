package com.example.trackanything.ui.screens

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.material3.Snackbar
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trackanything.models.Record
import com.example.trackanything.repository.ProjectRepository
import com.example.trackanything.repository.RecordRepository
import com.example.trackanything.utils.Helpers.finishActivity
import com.google.accompanist.insets.navigationBarsPadding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddRecordScreen(navController: NavController, projectRepository: ProjectRepository, recordRepository: RecordRepository, projectId: String) {

    val project = projectRepository.getLDById(projectId.toInt()).observeAsState(initial = null).value

    var value by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var isButtonEnabled by remember { mutableStateOf(true) } // Add this line

    var errorText by remember { mutableStateOf("") }

    val context = LocalContext.current
    val activity = context as? AppCompatActivity

    val coroutineScope = rememberCoroutineScope()

    Column( modifier = Modifier
        .fillMaxWidth()
        .padding(4.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TopAppBar(
            title = { Text(text = "Add record") },
            navigationIcon = {
                IconButton(onClick = {
                    finishActivity(coroutineScope, activity, navController)
                }) {
                    Icon(Icons.Filled.KeyboardArrowLeft, contentDescription = "Back")
                }
            },
            actions = {

            }
        )
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
                    val newRecord = Record(
                        id = 0,
                        projectId = projectId.toInt(),
                        value = value,
                        note = note,
                        timeAdded = System.currentTimeMillis()
                    )
                    CoroutineScope(Dispatchers.IO).launch {
                        recordRepository.insert(newRecord)
                        withContext(Dispatchers.Main) {
                            navController.popBackStack()
                        }
                        finishActivity(coroutineScope, activity, navController)
                    }

                    println("Record added: $newRecord")
                }
            },
            enabled = isButtonEnabled // Use the state variable here
        ) {
            Text("Add Record")
        }
    }


}
