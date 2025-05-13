package com.aman.cricmate.model

import java.util.Date

data class TestResult(
    val fieldName: String,
    val results: List<ResultEntry>
)

data class ResultEntry(
    val userId: String,
    val testDate: Date,
    val result: String
)
