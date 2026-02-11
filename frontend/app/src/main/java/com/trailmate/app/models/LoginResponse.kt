package com.trailmate.app.models

data class LoginResponse(
    val token: String,
    val userId: Int,
    val email: String,
    val message: String
)
