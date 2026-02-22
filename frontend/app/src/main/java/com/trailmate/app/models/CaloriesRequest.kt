package com.trailmate.app.models

data class CaloriesRequest(
    val activity: String,
    val weight: Double,
    val duration_hours: Double?,
    val distance_km: Double?
)
