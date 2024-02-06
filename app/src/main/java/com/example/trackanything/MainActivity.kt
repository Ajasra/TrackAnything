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
import com.example.trackanything.repository.NotificationRepository
import com.example.trackanything.repository.ProjectRepository
import com.example.trackanything.repository.RecordRepository
// Screens
import com.example.trackanything.ui.AddProjectScreen
import com.example.trackanything.ui.AddRecordScreen
import com.example.trackanything.ui.EditProjectScreen
import com.example.trackanything.ui.MainScreen
import com.example.trackanything.ui.ProjectScreen
// Theme
import com.example.trackanything.ui.theme.TrackAnythingTheme

/**
 * This is the main activity of the application.
 * It sets up the navigation and the repositories for the application.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the DAOs and repositories
        val projectDao = AppDatabase.getDatabase(this).projectDao()
        val projectRepository = ProjectRepository(projectDao)
        val recordDao = AppDatabase.getDatabase(this).recordDao()
        val recordRepository = RecordRepository(recordDao)
        val notificationDao = AppDatabase.getDatabase(this).notificationDao()
        val notificationRepository = NotificationRepository(notificationDao)

        setContent {
            // Set the theme for the application
            TrackAnythingTheme {
                // Create a surface with the background color of the theme
                Surface(color = MaterialTheme.colorScheme.background, modifier = Modifier.fillMaxSize()) {
                    // Create a navigation controller
                    val navController = rememberNavController()
                    // Set up the navigation host with the start destination as the main screen
                    NavHost(navController = navController, startDestination = "main_screen") {
                        // Define the composable for the main screen
                        composable("main_screen") {
                            MainScreen(navController, projectRepository)
                        }
                        // Define the composable for the add project screen
                        composable("addProject") { AddProjectScreen(navController, projectRepository, notificationRepository) }
                        // Define the composable for the add record screen
                        composable("addRecord/{projectId}") { backStackEntry ->
                            val projectId = backStackEntry.arguments?.getString("projectId")
                            if (projectId != null) {
                                AddRecordScreen(navController, projectRepository, recordRepository, projectId)
                            }else{
                                navController.popBackStack()
                            }
                        }
                        // Define the composable for the project screen
                        composable("projectScreen/{projectId}") { backStackEntry ->
                            val projectId = backStackEntry.arguments?.getString("projectId")
                            if (projectId != null) {
                                ProjectScreen(navController, projectRepository, recordRepository, projectId)
                            } else {
                                navController.popBackStack()
                            }
                        }
                        // Define the composable for the edit project screen
                        composable("editProject/{projectId}") { backStackEntry ->
                            val projectId = backStackEntry.arguments?.getString("projectId")
                            if (projectId != null) {
                                EditProjectScreen(navController, projectRepository, notificationRepository, projectId)
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

/**
 * This is a preview function for the main activity.
 * It is used by the Android Studio preview feature to display a preview of the UI in the design editor.
 */
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TrackAnythingTheme {

    }
}