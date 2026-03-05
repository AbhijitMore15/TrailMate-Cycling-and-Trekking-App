package com.trailmate.app.repository

import com.trailmate.app.models.Activity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object ActivityRepository {

    private val _activities = MutableStateFlow<List<Activity>>(emptyList())
    val activities: StateFlow<List<Activity>> = _activities

    fun addActivity(activity: Activity) {
        _activities.value = _activities.value + activity
    }
}