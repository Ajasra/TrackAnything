package com.example.trackanything.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * This is a Composable function that displays a header text.
 *
 * @param title The text to be displayed as the header.
 */
@Composable
fun MyHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.h4,
        modifier = Modifier.padding(16.dp)
    )
}