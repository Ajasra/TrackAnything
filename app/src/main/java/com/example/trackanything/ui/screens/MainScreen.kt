package com.example.trackanything.ui.screens

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.trackanything.models.Project
import com.example.trackanything.repository.ProjectRepository
import com.example.trackanything.ui.components.MyHeader
import com.google.accompanist.insets.navigationBarsPadding

@Composable
fun MainScreen(navController: NavController, projectRepository: ProjectRepository) {

    // Fetch all projects from the database
    val projects = projectRepository.getLDAll().observeAsState(initial = emptyList()).value

    Box(modifier = Modifier.fillMaxSize()) {
        MyHeader(title = "Track Anything")
        // Display all projects in a list

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
                    .padding(top = 80.dp, start = 16.dp, end = 16.dp, bottom = 0.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                LazyColumn(modifier = Modifier.weight(1f)) {
                    items(projects) { project ->
                        ProjectItem(project, navController) // Display each project
                    }
                }
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.Bottom
            ){
                FloatingActionButton(
                    onClick = { navController.navigate("addProject") },
                    modifier = Modifier
                        .padding(16.dp)
                        .navigationBarsPadding(),
                    content = {
                        Icon(Icons.Filled.Add, contentDescription = "Add")
                    }
                )
            }

        }
    }
}


@Composable
fun ProjectItem(project: Project, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Button(
            onClick = {
                // navigate to the project details screen
                navController.navigate("projectScreen/${project.id}")
            },
            modifier = Modifier
                .fillMaxWidth(.7f)
                .padding(0.dp, 0.dp, 2.dp, 0.dp)
        ) {
            Text(text = project.name)
        }

        Button(
            onClick = {
                navController.navigate("addRecord/${project.id}")
            },
            modifier = Modifier
                .fillMaxWidth(0.2f)
                .padding(2.dp, 0.dp, 0.dp, 0.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Add")
        }
    }
}