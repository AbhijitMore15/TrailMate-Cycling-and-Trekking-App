package com.trailmate.app.models

data class Route(
    val id: Int,
    val name: String,
    val distance: Double,
    val elevation: Double,
    val duration: Int,
    val latitude: Double,
    val longitude: Double,
    val coordinates: List<List<Double>>
)