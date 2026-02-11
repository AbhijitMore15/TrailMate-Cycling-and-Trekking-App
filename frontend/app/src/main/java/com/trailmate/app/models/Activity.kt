package com.trailmate.app.models

data class Activity(
    val id: Int,
    val routeId: Int,
    val userId: Int,
    val distance: Double,
    val time: Int,
    val steps: Int,
    val pedals: Int,
    val timestamp: String
)