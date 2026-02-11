package com.trailmate.app.models

data class EffortEstimation(
    val distance: Double,
    val estimatedSteps: Int,
    val estimatedPedals: Int,
    val estimatedCalories: Int
)