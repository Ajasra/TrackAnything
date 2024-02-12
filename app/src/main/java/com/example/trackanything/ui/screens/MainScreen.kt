package com.example.trackanything.ui.screens

import android.app.Activity
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trackanything.models.Project
import com.example.trackanything.repository.ProjectRepository
import com.example.trackanything.utils.Helpers.finishActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, projectRepository: ProjectRepository) {

    // Fetch all projects from the database
    val projects = projectRepository.getLDAll().observeAsState(initial = emptyList()).value
    val context = LocalContext.current
    val activity = context as? AppCompatActivity

    val coroutineScope = rememberCoroutineScope()


    Box(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text(text = "TrackAnything") },
            navigationIcon = {
                IconButton(onClick = {
                    finishActivity(coroutineScope, activity, navController)
                }) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Back")
                }
            },
            actions = {
                IconButton(
                    onClick = {
                        navController.navigate("addProject")
                    }) {
                    Icon(Icons.Filled.Add, contentDescription = "Add")
                }
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 0.dp), // Adjust this padding as needed
            verticalArrangement = Arrangement.Top
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
                    .padding(top = 80.dp, start = 8.dp, end = 8.dp, bottom = 0.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(projects) { project ->
                        ProjectItem(project, navController) // Display each project
                    }
                }
            }

        }
    }
}


@Composable
fun ProjectItem(project: Project, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Button(
            onClick = {
                navController.navigate("addRecord/${project.id}")
            },
            modifier = Modifier
                .fillMaxWidth(0.3f)
                .padding(0.dp)
        ) {
            Text(text = "ADD")
        }

        Button(
            onClick = {
                // navigate to the project details screen
                navController.navigate("projectScreen/${project.id}")
            },
            modifier = Modifier
                .fillMaxWidth(.7f)
                .padding(0.dp)
        ) {
            Text(text = project.name)
        }


    }
}