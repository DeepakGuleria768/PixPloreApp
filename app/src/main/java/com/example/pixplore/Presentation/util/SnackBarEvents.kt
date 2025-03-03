package com.example.pixplore.Presentation.util

import androidx.compose.material3.SnackbarDuration
import java.time.Duration

data class SnackBarEvents(
    val message : String,
    val duration: SnackbarDuration = SnackbarDuration.Short
) {
}