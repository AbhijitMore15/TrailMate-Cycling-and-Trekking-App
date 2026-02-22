package com.trailmate.app.repository

import com.trailmate.app.api.ApiClient
import com.trailmate.app.models.CaloriesRequest
import com.trailmate.app.models.CaloriesResponse
import com.trailmate.app.models.CaloriesHistory

class CaloriesRepository {

    private val api = ApiClient.caloriesApi

    // ================= CALCULATE =================
    suspend fun calculateCalories(
        userId: Int,
        request: CaloriesRequest
    ): Result<CaloriesResponse> {
        return try {

            val response = api.calculateCalories(userId, request)

            if (response.isSuccessful) {
                response.body()?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Empty response"))
            } else {
                Result.failure(Exception("API ${response.code()}"))
            }

        } catch (e: Exception) {
            Result.failure(Exception("Network error: ${e.message}"))
        }
    }


    // ================= HISTORY =================
    suspend fun getHistory(
        userId: Int
    ): Result<List<CaloriesHistory>> {
        return try {

            val response = api.getCaloriesHistory(userId)

            if (response.isSuccessful)
                Result.success(response.body() ?: emptyList())
            else
                Result.failure(Exception("API ${response.code()}"))

        } catch (e: Exception) {
            Result.failure(Exception("Network error: ${e.message}"))
        }
    }
}