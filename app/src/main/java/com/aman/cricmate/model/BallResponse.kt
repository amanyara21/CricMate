package com.aman.cricmate.model

import io.github.sceneview.math.Position

data class BallResponse(
    val _id: String,
    val userId: String,
    val date: String,
    val type: String,
    val speed: Double,
    val coordinates: List<Position>? = emptyList()
)

data class StatsData(
    val totalBalls: Int,
    val avgSpeed: Double,
    val yorkers: Int,
    val bouncers: Int,
    val short: Int,
    val goodLength: Int
)

data class DateWiseStats(
    val success: Boolean,
    val stats: List<DateStatsData>
)

data class DateStatsData(
    val date: String,
    val totalBalls: Int,
    val avgSpeed: Double,
    val yorkers: Int,
    val bouncers: Int,
    val short: Int,
    val goodLength: Int
)

