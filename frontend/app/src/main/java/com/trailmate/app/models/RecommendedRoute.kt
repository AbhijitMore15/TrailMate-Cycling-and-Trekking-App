package com.trailmate.app.models

data class RecommendedRoute(
    val route: Route,
    val score: Double,
    val reason: String
)