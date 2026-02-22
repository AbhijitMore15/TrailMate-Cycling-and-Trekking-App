package com.trailmate.app.api

import com.trailmate.app.models.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Path
interface CaloriesApiService {

    @POST("calories/calculate")
    suspend fun calculateCalories(
        @Query("user_id") userId: Int,
        @Body request: CaloriesRequest
    ): Response<CaloriesResponse>


    @GET("calories/history/{userId}")
    suspend fun getCaloriesHistory(
        @Path("userId") userId: Int
    ): Response<List<CaloriesHistory>>
}
