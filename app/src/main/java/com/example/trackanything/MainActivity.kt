package com.example.trackanything

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.trackanything.database.AppDatabase
import com.example.trackanything.repository.ProjectRepository
// Screens
import com.example.trackanything.ui.AddProjectScreen
import com.example.trackanything.ui.AddRecordScreen
import com.example.trackanything.ui.MainScreen
import com.example.trackanything.ui.ProjectScreen
// Theme
import com.example.trackanything.ui.theme.TrackAnythingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val projectDao = AppDatabase.getDatabase(this).projectDao()
        val projectRepository = ProjectRepository(projectDao)
        setContent {
            TrackAnythingTheme {
                Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "main_screen") {
                        composable("main_screen") {
                            MainScreen(navController, projectRepository)
                        }
                        composable("addProject") { AddProjectScreen(navController, projectRepository) }
                        composable("addRecord/{projectId}") { backStackEntry ->
                            val projectId = backStackEntry.arguments?.getString("projectId")
                            if (projectId != null) {
                                AddRecordScreen(navController, projectRepository, projectId)
                            }else{
                                navController.popBackStack()
                            }
                        }
                        composable("projectScreen/{projectId}") { backStackEntry ->
                            val projectId = backStackEntry.arguments?.getString("projectId")
                            if (projectId != null) {
                                ProjectScreen(navController, projectRepository, projectId)
                            } else {
                                navController.popBackStack()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TrackAnythingTheme {

    }
}