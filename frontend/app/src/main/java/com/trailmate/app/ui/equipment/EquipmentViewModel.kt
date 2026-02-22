package com.trailmate.app.ui.equipment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.trailmate.app.models.EquipmentRequest
import com.trailmate.app.models.EquipmentResponse
import com.trailmate.app.repository.EquipmentRepository
import kotlinx.coroutines.launch
import androidx.compose.runtime.*

class EquipmentViewModel : ViewModel() {

    private val repository = EquipmentRepository()

    var equipmentList by mutableStateOf<List<EquipmentResponse>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun getEquipment(
        activity: String,
        difficulty: String,
        duration: Int
    ) {

        viewModelScope.launch {

            isLoading = true
            errorMessage = null

            val result = repository.getEquipment(
                EquipmentRequest(
                    activity = activity,
                    difficulty = difficulty,
                    duration_hours = duration
                )
            )

            isLoading = false

            result.onSuccess {
                equipmentList = it
            }

            result.onFailure {
                errorMessage = it.message
            }
        }
    }
}
