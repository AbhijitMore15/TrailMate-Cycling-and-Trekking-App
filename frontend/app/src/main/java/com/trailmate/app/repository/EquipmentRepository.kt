package com.trailmate.app.repository

import com.trailmate.app.api.ApiClient
import com.trailmate.app.models.EquipmentRequest
import com.trailmate.app.models.EquipmentResponse

class EquipmentRepository {

    suspend fun getEquipment(
        request: EquipmentRequest
    ): Result<List<EquipmentResponse>> {

        return try {

            val response = ApiClient
                .equipmentApi
                .getEquipment(request)

            if (response.isSuccessful) {
                Result.success(response.body() ?: emptyList())
            } else {
                Result.failure(
                    Exception("API Error: ${response.code()}")
                )
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
