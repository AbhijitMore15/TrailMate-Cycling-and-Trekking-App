package com.trailmate.app.repository

import com.trailmate.app.api.ApiClient
import com.trailmate.app.api.WeeklyStat

class StatsRepository {

    suspend fun getWeekly(userId: Int): List<WeeklyStat> {
        val response = ApiClient.statsApi.getWeeklyStats(userId)
        return response.body() ?: emptyList()
    }
}
