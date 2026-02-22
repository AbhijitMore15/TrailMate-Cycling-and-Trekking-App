package com.trailmate.app.models

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String,

    @SerializedName("fitness_level")
    val fitnessLevel: String
)
