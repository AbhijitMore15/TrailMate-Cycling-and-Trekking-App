package com.trailmate.app.ui.calories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.trailmate.app.models.CaloriesRequest
import com.trailmate.app.models.CaloriesHistory
import com.trailmate.app.repository.CaloriesRepository
import kotlinx.coroutines.launch

class CaloriesViewModel : ViewModel() {

    private val repository = CaloriesRepository()

    var caloriesResult by mutableStateOf<Double?>(null)
        private set

    var historyList by mutableStateOf<List<CaloriesHistory>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set


    val totalCalories: Double
        get() = historyList.sumOf { it.calories }

    val avgCalories: Double
        get() = if (historyList.isNotEmpty())
            historyList.sumOf { it.calories } / historyList.size
        else 0.0


    // ================= CALCULATE =================
    fun calculateCalories(
        userId: Int,
        activity: String,
        duration: Double?,
        distance: Double?,
        weight: Double
    ) {

        viewModelScope.launch {

            isLoading = true
            errorMessage = null

            val result = repository.calculateCalories(
                userId,
                CaloriesRequest(
                    activity = activity,
                    weight = weight,
                    duration_hours = duration,
                    distance_km = distance
                )
            )

            isLoading = false

            result.onSuccess {
                caloriesResult = it.calories_burned
            }

            result.onFailure {
                errorMessage = it.message
                caloriesResult = null
            }
        }
    }


    // ================= HISTORY =================
    fun loadHistory(userId: Int) {

        viewModelScope.launch {

            isLoading = true

            val result = repository.getHistory(userId)

            isLoading = false

            result.onSuccess {
                historyList = it
            }

            result.onFailure {
                errorMessage = it.message
            }
        }
    }
}
