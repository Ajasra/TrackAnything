package com.example.trackanything.activities

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.compose.rememberNavController
import com.example.trackanything.database.AppDatabase
import com.example.trackanything.repository.ProjectRepository
import com.example.trackanything.repository.RecordRepository
import com.example.trackanything.ui.screens.AddRecordScreen

/**
 * DialogActivity is an activity that displays a dialog for adding a record to a project.
 * It initializes the project and record repositories and sets the content to the AddRecordScreen.
 */
class DialogActivity : AppCompatActivity() {
    // The project repository
    private lateinit var projectRepository: ProjectRepository
    // The record repository
    private lateinit var recordRepository: RecordRepository

    /**
     * This function is called when the activity is starting.
     * It initializes the project and record repositories and sets the content to the AddRecordScreen.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Note: Otherwise it is null.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the project repository with the project DAO from the database
        val projectDao = AppDatabase.getDatabase(this).projectDao()
        projectRepository = ProjectRepository(projectDao)

        // Initialize the record repository with the record DAO from the database
        val recordDao = AppDatabase.getDatabase(this).recordDao()
        recordRepository = RecordRepository(recordDao)

        // Get the project id from the intent extras
        val projectId = intent.getIntExtra("project_id", 0)

        // Set the content to the AddRecordScreen with the navController, projectRepository, recordRepository, and projectId
        setContent {
            val navController = rememberNavController()

            AddRecordScreen(navController, projectRepository, recordRepository, projectId.toString())
        }
    }
}