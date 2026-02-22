package com.trailmate.app.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trailmate.app.api.WeeklyStat
import com.trailmate.app.repository.StatsRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.*

class StatsViewModel : ViewModel() {

    private val repo = StatsRepository()

    var stats by mutableStateOf<List<WeeklyStat>>(emptyList())
        private set

    fun load(userId: Int) {
        viewModelScope.launch {
            stats = repo.getWeekly(userId)
        }
    }
}
