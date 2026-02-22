package com.trailmate.app.api

import com.trailmate.app.models.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface EquipmentApiService {

    @POST("equipment/recommend")
    suspend fun getEquipment(
        @Body request: EquipmentRequest
    ): Response<List<EquipmentResponse>>
}
