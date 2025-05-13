package com.aman.cricmate.model

import java.util.Date

data class PlayerReviewResponse(
    val reviews: List<PlayerReview>
)

data class PlayerReview(
    val coach: CoachInfo,
    val review: String,
    val date: Date
)

data class CoachInfo(
    val _id: String,
    val name: String
)

data class PlayerReviewRequest(
    val player: String,
    val review: String
)