package com.example.trackanything.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.trackanything.database.AppDatabase
import com.example.trackanything.repository.ProjectRepository
import com.example.trackanything.repository.RecordRepository
import com.example.trackanything.ui.screens.AddRecordScreen

/**
 * This activity is responsible for displaying the project screen.
 * It initializes the project and record repositories and sets the content to the ProjectScreen composable.
 */
class ProjectActivity : ComponentActivity() {

    // The project repository
    private lateinit var projectRepository: ProjectRepository
    // The record repository
    private lateinit var recordRepository: RecordRepository

    /**
     * Called when the activity is starting.
     * This is where most initialization should go.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the project repository
        val projectDao = AppDatabase.getDatabase(this).projectDao()
        projectRepository = ProjectRepository(projectDao)

        // Initialize the record repository
        val recordDao = AppDatabase.getDatabase(this).recordDao()
        recordRepository = RecordRepository(recordDao)

        // Get the project id from the intent extras
        val projectId = intent.getIntExtra("project_id", 0)

        // Set the content to the ProjectScreen composable
        setContent {
            ProjectScreen(projectRepository, recordRepository, projectId.toString())
        }
    }

    /**
     * This composable displays the project screen.
     * It creates a NavController and displays the AddRecordScreen composable.
     *
     * @param projectRepository The project repository.
     * @param recordRepository The record repository.
     * @param projectId The id of the project.
     */
    @Composable
    fun ProjectScreen(projectRepository: ProjectRepository, recordRepository: RecordRepository, projectId: String) {
        // Create a NavController
        val navController = rememberNavController()
        // Display the AddRecordScreen composable
        AddRecordScreen(navController, projectRepository, recordRepository, projectId)
    }
}