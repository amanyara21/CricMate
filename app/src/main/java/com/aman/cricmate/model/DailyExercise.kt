package com.aman.cricmate.model

data class Exercise(
    val name: String,
    val duration: String? = null,
    val reps: String? = null
)

data class DailyExercise(
    val day: String,
    val exercises: List<Exercise>
)
