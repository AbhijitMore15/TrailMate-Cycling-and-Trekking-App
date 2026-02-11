package com.trailmate.app.models

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    val email: String,
    val password: String,
    val name: String,

    // 🔥 THIS IS THE KEY FIX
    @SerializedName("fitness_level")
    val fitnessLevel: String
)
