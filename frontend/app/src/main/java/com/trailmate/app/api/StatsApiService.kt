package com.trailmate.app.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

data class WeeklyStat(
    val label: String,
    val distance: Float
)

interface StatsApiService {

    @GET("stats/weekly-distance/{userId}")
    suspend fun getWeeklyStats(
        @Path("userId") userId: Int
    ): Response<List<WeeklyStat>>
}
