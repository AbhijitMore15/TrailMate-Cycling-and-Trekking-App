package com.trailmate.app.api

import com.trailmate.app.models.User
import retrofit2.http.GET
import retrofit2.http.Header

interface UserApiService {

    @GET("auth/me")
    suspend fun getCurrentUser(
        @Header("Authorization") token: String
    ): User
}
