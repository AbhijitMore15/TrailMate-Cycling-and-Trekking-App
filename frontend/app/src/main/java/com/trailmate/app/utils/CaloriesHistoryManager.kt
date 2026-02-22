package com.trailmate.app.utils

import com.trailmate.app.models.CaloriesHistory

class CaloriesHistoryManager {

    private val historyList = mutableListOf<CaloriesHistory>()

    fun addHistory(history: CaloriesHistory) {
        historyList.add(history)
    }

    fun getHistory(): List<CaloriesHistory> {
        return historyList
    }
}
