package com.trailmate.app.repository

import com.trailmate.app.api.ApiClient
import com.trailmate.app.models.User
import com.trailmate.app.utils.DataStoreManager

class UserRepository(
    private val dataStore: DataStoreManager
) {

    suspend fun fetchUser(): User? {
        return try {

            val token = dataStore.getAuthTokenOnce()
                ?: return null

            // ✅ FIXED HERE
            ApiClient.userApi.getCurrentUser("Bearer $token")

        } catch (e: Exception) {
            null
        }
    }
}
